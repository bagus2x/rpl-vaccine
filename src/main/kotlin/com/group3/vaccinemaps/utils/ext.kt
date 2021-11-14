package com.group3.vaccinemaps.utils

import com.group3.vaccinemaps.entity.User
import com.group3.vaccinemaps.security.service.UserDetailsImpl
import org.springframework.security.core.Authentication
import org.springframework.security.access.AccessDeniedException

val Authentication.user: User
    get() {
        return try {
            (this.principal as UserDetailsImpl).user
        } catch (e: Exception) {
            throw AccessDeniedException("Access denied")
        }
    }