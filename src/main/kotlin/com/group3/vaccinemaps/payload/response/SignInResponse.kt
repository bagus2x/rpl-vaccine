package com.group3.vaccinemaps.payload.response

data class SignInResponse(
    val token: String,
    val user: UserResponse
)