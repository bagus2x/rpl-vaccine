package com.group3.vaccinemaps.repository

import com.group3.vaccinemaps.entity.ERole
import com.group3.vaccinemaps.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: ERole): Role?

    fun existsByName(name: ERole): Boolean
}