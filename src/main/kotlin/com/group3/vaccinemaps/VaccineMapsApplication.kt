package com.group3.vaccinemaps

import com.group3.vaccinemaps.payload.request.CreateRoleRequest
import com.group3.vaccinemaps.payload.request.SignUpRequest
import com.group3.vaccinemaps.services.RoleService
import com.group3.vaccinemaps.services.UserService
import com.group3.vaccinemaps.utils.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class VaccineMapsApplication {
    val log = logger()

    @Bean
    fun run(roleService: RoleService, userService: UserService) = CommandLineRunner {
        try {
            roleService.create(CreateRoleRequest("user"))
            roleService.create(CreateRoleRequest("admin"))

            userService.signUpAdmin(
                SignUpRequest(
                    name = "Host",
                    email = "host@gmail.com",
                    password = "host123",
                    gender = "FEMALE",
                    dateOfBirth = System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            log.error(e.message)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<VaccineMapsApplication>(*args)
}
