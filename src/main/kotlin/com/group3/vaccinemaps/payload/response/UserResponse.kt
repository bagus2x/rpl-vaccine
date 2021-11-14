package com.group3.vaccinemaps.payload.response

import com.group3.vaccinemaps.entity.Role
import java.util.*

data class UserResponse(
    val id: Long,
    val roles: Set<Role> = emptySet(),
    val photo: String? = null,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val kk: String? = null,
    val dateOfBirth: Long
)