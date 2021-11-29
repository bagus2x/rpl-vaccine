package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.SignInRequest
import com.group3.vaccinemaps.payload.request.SignUpRequest
import com.group3.vaccinemaps.payload.request.UpdateKKRequest
import com.group3.vaccinemaps.payload.response
import com.group3.vaccinemaps.payload.response.ParticipantResponse
import com.group3.vaccinemaps.payload.response.SignInResponse
import com.group3.vaccinemaps.payload.response.SignUpResponse
import com.group3.vaccinemaps.payload.response.UserResponse
import com.group3.vaccinemaps.services.UserService
import com.group3.vaccinemaps.utils.user
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class UserController(private val userService: UserService) {

    @PostMapping(
        value = ["/user/signin"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun signIn(@RequestBody req: SignInRequest): WebResponse<SignInResponse> {
        val res = userService.signIn(req)

        return response(200, "Ok", res)
    }

    @PostMapping(
        value = ["/user/signup"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun signup(@RequestBody req: SignUpRequest): WebResponse<SignUpResponse> {
        val res = userService.signUp(req)

        return response(201, "Created", res)
    }

    @GetMapping(
        value = ["/user/{userId}"],
        produces = ["application/json"],
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun getById(@PathVariable userId: String): WebResponse<UserResponse> {
        val uId = userId.toLongOrNull() ?: throw BadRequestException("Invalid user id")
        val res = userService.getById(uId)

        return response(201, "Created", res)
    }

    @GetMapping(
        value = ["/user"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('USER')")
    fun getAuthenticatedUser(authentication: Authentication): WebResponse<UserResponse> {
        val user = authentication.user

        return response(
            200, "Ok", UserResponse(
                user.id,
                user.roles,
                user.photo,
                user.name,
                user.email,
                user.phoneNumber,
                user.kk,
                user.gender.name,
                user.dateOfBirth.time
            )
        )
    }

    @PatchMapping(
        value = ["/user/kk/change"],
        produces = ["application/json"],
    )
    @PreAuthorize("hasRole('USER')")
    fun updateKK(authentication: Authentication,@RequestBody req: UpdateKKRequest): WebResponse<UserResponse> {
        if (req.userId != authentication.user.id) throw AccessDeniedException("Access denied")
        val res = userService.updateKK(req)

        return response(200, "Ok", res)
    }
}