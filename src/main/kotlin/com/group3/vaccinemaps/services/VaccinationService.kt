package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.Vaccination
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.CreateVaccinationRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response.VaccinationResponse
import com.group3.vaccinemaps.repository.VaccinationRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class VaccinationService(val vaccinationRepository: VaccinationRepository, val validation: Validation) {

    fun create(req: CreateVaccinationRequest): VaccinationResponse {
        validation.validate(req)

        val vaccination = Vaccination(
            title = req.title!!,
            vaccine = req.vaccine!!,
            description = req.description,
            picture = req.picture,
            startDate = Date(req.startDate!!),
            lastDate = Date(req.lastDate!!),
        )

        vaccinationRepository.save(vaccination)

        return mapVaccinationToResponse(vaccination)
    }

    fun getById(vaccinationId: Long): VaccinationResponse {
        val vcn = vaccinationRepository.findByIdOrNull(vaccinationId) ?: throw NotFoundException("Vaccination not found")

        return mapVaccinationToResponse(vcn)
    }

    fun list(req: PaginationRequest): List<VaccinationResponse> {
        val page = vaccinationRepository.findAll(PageRequest.of(req.page, req.size))

        return page.fold(mutableListOf()) { accumulator, item -> accumulator.add(mapVaccinationToResponse(item)); accumulator }
    }

    fun delete(vaccinationId: Long) {
        val vcn = vaccinationRepository.findByIdOrNull(vaccinationId) ?: throw NotFoundException("Vaccination not found")

        vaccinationRepository.deleteById(vcn.id)
    }

    private fun mapVaccinationToResponse(vaccination: Vaccination) = VaccinationResponse(
        vaccination.id,
        vaccination.title,
        vaccination.vaccine,
        vaccination.description,
        vaccination.picture,
        vaccination.startDate.time,
        vaccination.lastDate.time,
        vaccination.createdAt.time,
        vaccination.updatedAt.time
    )
}