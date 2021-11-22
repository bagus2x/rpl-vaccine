package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateNotificationRequest(
    val picture: String?,
    @field:NotNull
    @field:Min(0)
    val receiverId: Long?,
    @field:NotBlank
    @field:NotNull
    val title: String?,
    val content: String?,
    @field:NotNull
    val email: Boolean
)

