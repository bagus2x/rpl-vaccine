package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.ERole
import com.group3.vaccinemaps.entity.Role
import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.exception.ConflictException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.CreateRoleRequest
import com.group3.vaccinemaps.payload.response.RoleResponse
import com.group3.vaccinemaps.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleService(private val roleRepository: RoleRepository, private val validation: Validation) {

    fun create(req: CreateRoleRequest): RoleResponse {
        validation.validate(req)

        val name = when (req.name?.lowercase()) {
            "admin" -> ERole.ROLE_ADMIN
            "user" -> ERole.ROLE_USER
            else -> throw BadRequestException("Bad request")
        }

        val exists = roleRepository.existsByName(name)
        if (exists) throw ConflictException("Role already exists")

        val role = roleRepository.save(Role(name = name))

        return RoleResponse(role.id, role.name.name)
    }
}