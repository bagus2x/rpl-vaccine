package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.*

data class SignInRequest(
    @field:NotBlank
    @field:NotNull
    @field:Email
    val email: String?,
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 5, max = 128)
    val password: String?
)
