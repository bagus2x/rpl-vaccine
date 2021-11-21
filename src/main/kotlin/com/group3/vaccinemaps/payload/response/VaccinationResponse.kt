package com.group3.vaccinemaps.payload.response

data class VaccinationResponse(
    val id: Long,
    val title: String,
    val vaccine: String,
    val description: String?,
    val picture: String?,
    val startDate: Long,
    val lastDate: Long,
    val numberOfParticipants: Int,
    val createdAt: Long,
    val updatedAt: Long
)
