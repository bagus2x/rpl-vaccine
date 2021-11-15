package com.group3.vaccinemaps.utils

import com.group3.vaccinemaps.entity.User
import com.group3.vaccinemaps.security.service.UserDetailsImpl
import org.springframework.security.core.Authentication
import org.springframework.security.access.AccessDeniedException

val Authentication.user: User
    get() {
        return try {
            (principal as UserDetailsImpl).user
        } catch (e: Exception) {
            throw AccessDeniedException("Access denied")
        }
    }

fun Authentication.hasRole(name: String): Boolean {
    return user.roles.map { it.name.name }.contains("ROLE_$name")
}