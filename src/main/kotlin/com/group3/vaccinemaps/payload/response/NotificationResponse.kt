package com.group3.vaccinemaps.payload.response

import com.group3.vaccinemaps.entity.ENotificationStatus
import java.util.*

data class NotificationResponse(
    val id: Long,
    val picture: String?,
    val receiver: Receiver,
    val title: String,
    val content: String?,
    val status: String,
    val createdAt: Date
) {

    data class Receiver(
        val id: Long,
        val photo: String?,
        val name: String
    )
}

