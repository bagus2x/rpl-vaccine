package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.ERole
import com.group3.vaccinemaps.entity.User
import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.exception.ConflictException
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.SignInRequest
import com.group3.vaccinemaps.payload.request.SignUpRequest
import com.group3.vaccinemaps.payload.response.SignInResponse
import com.group3.vaccinemaps.payload.response.SignUpResponse
import com.group3.vaccinemaps.payload.response.UserResponse
import com.group3.vaccinemaps.repository.RoleRepository
import com.group3.vaccinemaps.repository.UserRepository
import com.group3.vaccinemaps.security.jwt.JwtUtils
import com.group3.vaccinemaps.security.service.UserDetailsImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val validation: Validation
) {

    fun signIn(req: SignInRequest): SignInResponse {
        validation.validate(req)

        try {
            val auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
            SecurityContextHolder.getContext().authentication = auth

            val token = jwtUtils.generateJwtToken(auth)
            val user = (auth.principal as UserDetailsImpl).user

            return SignInResponse(token, mapUserEntityToResponse(user))
        } catch (e: AuthenticationException) {
            throw BadRequestException("Password or email invalid")
        }
    }

    fun signUp(req: SignUpRequest): SignUpResponse {
        validation.validate(req)

        if (userRepository.existsByEmail(req.email!!)) throw ConflictException("Email already exists")
        val roleUser = roleRepository.findByName(ERole.ROLE_USER) ?: throw NotFoundException("Role user not found")

        var user = User(
            roles = setOf(roleUser),
            name = req.name!!,
            email = req.email,
            password = passwordEncoder.encode(req.password),
            dateOfBirth = Date(req.dateOfBirth ?: 0)
        )

        user = userRepository.save(user)
        val token = jwtUtils.generateJwtToken(user)

        return SignUpResponse(token, mapUserEntityToResponse(user))
    }

    fun signUpAdmin(req: SignUpRequest): SignUpResponse {
        validation.validate(req)

        if (userRepository.existsByEmail(req.email!!)) throw ConflictException("Email already exists")
        val roleUser = roleRepository.findByName(ERole.ROLE_USER) ?: throw NotFoundException("Role user not found")
        val roleAdmin = roleRepository.findByName(ERole.ROLE_ADMIN) ?: throw NotFoundException("Role admin not found")

        var user = User(
            roles = setOf(roleUser, roleAdmin),
            name = req.name!!,
            email = req.email,
            password = passwordEncoder.encode(req.password),
            dateOfBirth = Date(req.dateOfBirth ?: 0)
        )

        user = userRepository.save(user)
        val token = jwtUtils.generateJwtToken(user)

        return SignUpResponse(token, mapUserEntityToResponse(user))
    }

    private fun mapUserEntityToResponse(user: User) = UserResponse(
        user.id,
        user.roles,
        user.photo,
        user.name,
        user.email,
        user.phoneNumber,
        user.kk,
        user.dateOfBirth.time
    )
}