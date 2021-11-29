package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateKKRequest(
    @field:NotNull
    @field:Min(0)
    val userId: Long?,
    @field:NotNull
    @NotBlank
    val kk: String?
)