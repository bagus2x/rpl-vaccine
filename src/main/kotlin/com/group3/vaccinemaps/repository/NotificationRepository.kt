package com.group3.vaccinemaps.repository

import com.group3.vaccinemaps.entity.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {

    fun findAllByReceiverIdOrderByCreatedAtDesc(userId: Long, page: Pageable): List<Notification>

    fun findByIdAndReceiverId(notificationId: Long, receiverId: Long): Notification?
}