package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.request.RegisterParticipantRequest
import com.group3.vaccinemaps.payload.response
import com.group3.vaccinemaps.payload.response.ParticipantResponse
import com.group3.vaccinemaps.services.ParticipantService
import com.group3.vaccinemaps.utils.hasRole
import com.group3.vaccinemaps.utils.user
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class ParticipantController(private val participantService: ParticipantService) {

    @PostMapping(
        value = ["/participant"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun register(@RequestBody req: RegisterParticipantRequest): WebResponse<ParticipantResponse> {
        val res = participantService.register(req)

        return response(201, "Created", res)
    }

    @GetMapping(
        value = ["/participant/{participantId}"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun getById(authentication: Authentication, @PathVariable participantId: String): WebResponse<ParticipantResponse> {
        val pId = participantId.toLongOrNull() ?: throw BadRequestException("Invalid participant id")

        val res = when (authentication.hasRole("ADMIN")) {
            true -> participantService.getById(pId)
            else -> participantService.getByIdAndUserId(pId, authentication.user.id)
        }

        return response(200, "Ok", res)
    }

    @GetMapping(
        value = ["/participants"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('USER')")
    fun list(
        authentication: Authentication,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): WebResponse<List<ParticipantResponse>> {
        val req = PaginationRequest(page, size)

        val res = when (authentication.hasRole("ADMIN")) {
            true -> participantService.list(req)
            else -> participantService.listByUserId(req, authentication.user.id)
        }

        return response(200, "Ok", res)
    }

    @PatchMapping(
        value = ["/participant/{participantId}/accept"],
        produces = ["application/json"],
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun accept(@PathVariable participantId: String): WebResponse<ParticipantResponse> {
        val pId = participantId.toLongOrNull() ?: throw BadRequestException("Invalid participant id")
        val res = participantService.accept(pId)

        return response(200, "Ok", res)
    }

    @PatchMapping(
        value = ["/participant/{participantId}/reject"],
        produces = ["application/json"],
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun reject(@PathVariable participantId: String): WebResponse<ParticipantResponse> {
        val pId = participantId.toLongOrNull() ?: throw BadRequestException("Invalid participant id")
        val res = participantService.reject(pId)

        return response(200, "Ok", res)
    }

    @PatchMapping(
        value = ["/participant/{participantId}/cancel"],
        produces = ["application/json"],
    )
    @PreAuthorize("hasRole('USER')")
    fun cancel(authentication: Authentication, @PathVariable participantId: String): WebResponse<ParticipantResponse> {
        val pId = participantId.toLongOrNull() ?: throw BadRequestException("Invalid participant id")
        val res = participantService.cancel(pId, authentication.user.id)

        return response(200, "Ok", res)
    }
}