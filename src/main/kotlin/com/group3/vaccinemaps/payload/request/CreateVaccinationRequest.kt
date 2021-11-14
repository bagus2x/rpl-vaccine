package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateVaccinationRequest(
    @field:NotBlank
    @field:NotNull
    @field:Size(max = 255)
    val title: String?,
    @field:NotBlank
    @field:NotNull
    val vaccine: String?,
    val description: String?,
    val picture: String?,
    @field:NotNull
    @field:Min(0)
    val startDate: Long?,
    @field:NotNull
    @field:Min(0)
    val lastDate: Long?
)
