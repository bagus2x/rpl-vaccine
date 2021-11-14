package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.*

data class CreateRoleRequest(
    @field:NotBlank
    @field:NotNull
    @Size(min = 5, max = 5)
    val name: String?
)