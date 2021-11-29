package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.ENotificationStatus
import com.group3.vaccinemaps.entity.Notification
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.CreateNotificationRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response.NotificationResponse
import com.group3.vaccinemaps.repository.NotificationRepository
import com.group3.vaccinemaps.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val validation: Validation
) {
    @Value("\${vaccine.maps.email}")
    private lateinit var email: String

    @Value("\${vaccine.maps.password}")
    private lateinit var password: String

    fun create(req: CreateNotificationRequest): NotificationResponse {
        validation.validate(req)

        val notification = createNotification(req)
        if (!req.email) return mapNotificationToResponse(notification)

        sendEmail(notification.receiver.email, req)

        return mapNotificationToResponse(notification)
    }

    private fun createNotification(req: CreateNotificationRequest): Notification {

        val receiver = userRepository.findByIdOrNull(req.receiverId) ?: throw NotFoundException("User not found")
        val notification = Notification(
            picture = req.picture,
            receiver = receiver,
            title = req.title ?: "",
            content = req.content ?: "",
            status = ENotificationStatus.UNSEEN,
            createdAt = Date()
        )

        return notificationRepository.save(notification)
    }

    private fun sendEmail(receiver: String, req: CreateNotificationRequest) {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val session = Session.getInstance(props, object : javax.mail.Authenticator() {

            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, "vaccinemaps123")
            }
        })

        val msg: Message = MimeMessage(session)
        msg.setFrom(InternetAddress(password, false))

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver))
        msg.subject = req.title
        msg.setContent(req.content, "text/html")
        msg.sentDate = Date()

        Transport.send(msg)
    }

    fun getById(notificationId: Long, receiverId: Long): NotificationResponse {
        val notification = notificationRepository.findByIdAndReceiverId(notificationId, receiverId)
            ?: throw NotFoundException("Notification not found")

        return mapNotificationToResponse(notification)
    }

    fun list(req: PaginationRequest, receiverId: Long): List<NotificationResponse> {
        val page = notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId, PageRequest.of(req.page, req.size))

        return page.fold(mutableListOf()) { accumulator, item -> accumulator.add(mapNotificationToResponse(item)); accumulator }
    }

    fun see(notificationId: Long, receiverId: Long): NotificationResponse {
        val notification = notificationRepository.findByIdOrNull(notificationId)
            ?: throw NotFoundException("Notification not found")
        if (notification.receiver.id != receiverId) throw NotFoundException("Notification not found")

        notification.status = ENotificationStatus.SEEN

        notificationRepository.save(notification)

        return mapNotificationToResponse(notification)
    }

    private fun mapNotificationToResponse(notification: Notification) = NotificationResponse(
        notification.id,
        notification.picture,
        NotificationResponse.Receiver(
            notification.receiver.id,
            notification.receiver.photo,
            notification.receiver.name
        ),
        notification.title,
        notification.content,
        notification.status.name,
        notification.createdAt
    )
}