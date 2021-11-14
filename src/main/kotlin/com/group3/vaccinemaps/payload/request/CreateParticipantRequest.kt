package com.group3.vaccinemaps.payload.request

data class CreateParticipantRequest(
    val userId: Long?,
    val vaccinationId: Long?
)
