package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.payload.Payload
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.CreateVaccinationRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response.VaccinationResponse
import com.group3.vaccinemaps.services.VaccinationService
import com.group3.vaccinemaps.utils.user
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class VaccinationController(private val vaccinationService: VaccinationService) {

    @PostMapping(
        value = ["/vaccination"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@RequestBody req: CreateVaccinationRequest): WebResponse<VaccinationResponse> {
        val article = vaccinationService.create(req)

        return WebResponse.ok(Payload(201, "Created", article))
    }

    @GetMapping(
        value = ["/vaccination/{vaccinationId}"],
        produces = ["application/json"]
    )
    fun getById(@PathVariable vaccinationId: String): WebResponse<VaccinationResponse> {
        val vId = vaccinationId.toLongOrNull() ?: throw BadRequestException("Invalid vaccination id")
        val article = vaccinationService.getById(vId)

        return WebResponse.ok(Payload(200, "Ok", article))
    }

    @GetMapping(
        value = ["/vaccinations"]
    )
    fun list(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): WebResponse<List<VaccinationResponse>> {
        val req = PaginationRequest(page, size)
        val articles = vaccinationService.list(req)

        return WebResponse.ok(Payload(200, "Ok", articles))
    }

    @DeleteMapping(
        value = ["/vaccination/{vaccinationId}"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable vaccinationId: String): WebResponse<Long> {
        val vId = vaccinationId.toLongOrNull() ?: throw BadRequestException("Invalid vaccination id")

        vaccinationService.delete(vId)

        return WebResponse.ok(Payload(200, "Ok", vId))
    }
}