package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.*

data class SignUpRequest(
    @field:NotBlank
    @field:NotNull
    val name: String?,
    @field:NotBlank
    @field:NotNull
    @field:Email
    val email: String?,
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 5, max = 128)
    val password: String?,
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 4, max = 6)
    val gender: String?,
    @field:Min(0)
    @field:NotNull
    val dateOfBirth: Long?
)
