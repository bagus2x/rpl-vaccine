package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class RegisterParticipantRequest(
    @field:NotNull
    @field:Min(0)
    val userId: Long?,
    @field:NotNull
    @field:Min(0)
    val vaccinationId: Long?
)
