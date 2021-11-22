package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.CreateNotificationRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response
import com.group3.vaccinemaps.payload.response.NotificationResponse
import com.group3.vaccinemaps.services.NotificationService
import com.group3.vaccinemaps.utils.user
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class NotificationController(private val notificationService: NotificationService) {

    @PostMapping(
        value = ["/notification"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody req: CreateNotificationRequest): WebResponse<NotificationResponse> {
        val res = notificationService.create(req)

        return response(201, "Created", res)
    }

    @GetMapping(
        value = ["/notification/{notificationId}"],
        produces = ["application/json"]
    )
    fun getById(authentication: Authentication, @PathVariable notificationId: String): WebResponse<NotificationResponse> {
        val nId = notificationId.toLongOrNull() ?: throw BadRequestException("Invalid notification id")
        val res = notificationService.getById(nId, authentication.user.id)

        return response(200, "Ok", res)
    }

    @GetMapping(
        value = ["/notifications"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('USER')")
    fun list(
        authentication: Authentication,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): WebResponse<List<NotificationResponse>> {
        val req = PaginationRequest(page, size)
        val res = notificationService.list(req, authentication.user.id)

        return response(200, "Ok", res)
    }
}