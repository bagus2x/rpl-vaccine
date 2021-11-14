package com.group3.vaccinemaps.payload.response

data class SignUpResponse(
    val token: String,
    val user: UserResponse
)