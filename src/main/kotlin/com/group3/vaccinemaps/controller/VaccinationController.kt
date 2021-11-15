package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.CreateVaccinationRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response
import com.group3.vaccinemaps.payload.response.VaccinationResponse
import com.group3.vaccinemaps.services.VaccinationService
import org.springframework.security.access.prepost.PreAuthorize
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
        val res = vaccinationService.create(req)

        return response(201, "Created", res)
    }

    @GetMapping(
        value = ["/vaccination/{vaccinationId}"],
        produces = ["application/json"]
    )
    fun getById(@PathVariable vaccinationId: String): WebResponse<VaccinationResponse> {
        val vId = vaccinationId.toLongOrNull() ?: throw BadRequestException("Invalid vaccination id")
        val res = vaccinationService.getById(vId)

        return response(200, "Ok", res)
    }

    @GetMapping(
        value = ["/vaccinations"],
        produces = ["application/json"]
    )
    fun list(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): WebResponse<List<VaccinationResponse>> {
        val req = PaginationRequest(page, size)
        val res = vaccinationService.list(req)

        return response(200, "Ok", res)
    }

    @DeleteMapping(
        value = ["/vaccination/{vaccinationId}"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable vaccinationId: String): WebResponse<Long> {
        val vId = vaccinationId.toLongOrNull() ?: throw BadRequestException("Invalid vaccination id")

        vaccinationService.delete(vId)

        return response(200, "Ok", vId)
    }
}