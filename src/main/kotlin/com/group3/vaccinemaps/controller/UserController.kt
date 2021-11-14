package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.payload.Payload
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.SignInRequest
import com.group3.vaccinemaps.payload.request.SignUpRequest
import com.group3.vaccinemaps.payload.response.SignInResponse
import com.group3.vaccinemaps.payload.response.SignUpResponse
import com.group3.vaccinemaps.services.UserService
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
        return WebResponse.ok(Payload(200, "Ok", res))
    }

    @PostMapping(
        value = ["/user/signup"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun signup(@RequestBody req: SignUpRequest): WebResponse<SignUpResponse> {
        val res = userService.signUp(req)
        return WebResponse.ok(Payload(201, "Created", res))
    }
}