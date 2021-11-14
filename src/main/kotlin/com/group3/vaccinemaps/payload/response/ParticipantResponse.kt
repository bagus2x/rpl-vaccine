package com.group3.vaccinemaps.payload.response

import com.group3.vaccinemaps.entity.EParticipantStatus
import java.util.*

data class ParticipantResponse(
    val id: Long,
    val user: User,
    val vaccination: Vaccination,
    val status: EParticipantStatus,
    val createdAt: Long,
    val updatedAt: Long
) {

    data class User(
        val id: Long,
        val photo: String?,
        val name: String
    )

    data class Vaccination(
        val id: Long,
        val title: String,
        val vaccine: String,
        val description: String?,
        val picture: String?,
        val startDate: Long,
        val lastDate: Long,
    )
}
