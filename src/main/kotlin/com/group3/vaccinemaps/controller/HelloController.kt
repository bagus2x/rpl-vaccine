package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.repository.UserRepository
import com.group3.vaccinemaps.utils.user
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class HelloController(val userRepository: UserRepository) {

    @GetMapping("/all")
    fun all(): Any {
        val user = userRepository.findAll()[0]

        return user.roles
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun user(authentication: Authentication): String {
        val user = authentication.user
        return user.email
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun admin() = "Halo Admin"
}