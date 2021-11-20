package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.EParticipantStatus.*
import com.group3.vaccinemaps.entity.Participant
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.exception.UnprocessableException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.RegisterParticipantRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response.ParticipantResponse
import com.group3.vaccinemaps.repository.ParticipantRepository
import com.group3.vaccinemaps.repository.UserRepository
import com.group3.vaccinemaps.repository.VaccinationRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ParticipantService(
    private val participantRepository: ParticipantRepository,
    private val userRepository: UserRepository,
    private val vaccinationRepository: VaccinationRepository,
    private val validation: Validation
) {

    fun register(req: RegisterParticipantRequest): ParticipantResponse {
        validation.validate(req)

        val participants = participantRepository.findTop20ByUserId(req.userId!!)
        if (participants.size == 20) throw UnprocessableException("Maximum limit exceeded. Please contact admin")

        var (accepted, waiting) = listOf(0, 0, 0, 0)
        participants.forEach {
            if (it.status == ACCEPTED) accepted++
            if (it.status == WAITING) waiting++
            if (it.vaccination.id == req.vaccinationId) throw UnprocessableException("User already registered")
        }

        if (accepted >= 2) throw UnprocessableException("User already registered")
        if (waiting >= 2) throw UnprocessableException("Number of waiting status is $waiting")
        if (accepted == 1 && waiting == 1) throw UnprocessableException("Number of accepted and rejected statuses are ${accepted + waiting}")

        val user = userRepository.findByIdOrNull(req.userId) ?: throw NotFoundException("User not found")
        val vaccination = vaccinationRepository.findByIdOrNull(req.vaccinationId) ?: throw NotFoundException("User not found")

        if (vaccination.lastDate.time < System.currentTimeMillis()) throw UnprocessableException("Registration closed")

        val participant = Participant(
            user = user,
            vaccination = vaccination,
            status = WAITING
        )

        participantRepository.save(participant)

        return mapParticipantToResponse(participant)
    }

    fun getById(participantId: Long): ParticipantResponse {
        val participant = participantRepository.getById(participantId)

        return mapParticipantToResponse(participant)
    }

    fun getByIdAndUserId(participantId: Long, userId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdAndUserId(participantId, userId)
            ?: throw NotFoundException("Participant not found")

        return mapParticipantToResponse(participant)
    }

    fun list(req: PaginationRequest): List<ParticipantResponse> {
        val page = participantRepository.findAll(PageRequest.of(req.page, req.size))

        return page.fold(mutableListOf()) { accumulator, item -> accumulator.add(mapParticipantToResponse(item)); accumulator }
    }

    fun listByUserId(req: PaginationRequest, userId: Long): List<ParticipantResponse> {
        val page = participantRepository.findAllByUserId(userId, PageRequest.of(req.page, req.size))

        return page.fold(mutableListOf()) { accumulator, item -> accumulator.add(mapParticipantToResponse(item)); accumulator }
    }

    fun accept(participantId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdOrNull(participantId) ?: throw NotFoundException("Participant not found")
        participant.status = ACCEPTED
        participantRepository.save(participant)

        return mapParticipantToResponse(participant)
    }

    fun reject(participantId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdOrNull(participantId) ?: throw NotFoundException("Participant not found")
        participant.status = REJECTED
        participantRepository.save(participant)

        return mapParticipantToResponse(participant)
    }

    fun cancel(participantId: Long, userId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdOrNull(participantId) ?: throw NotFoundException("Participant not found")
        if (participant.user.id != userId) throw AccessDeniedException("Access denied")

        participant.status = CANCELED
        participantRepository.save(participant)

        return mapParticipantToResponse(participant)
    }

    private fun mapParticipantToResponse(participant: Participant): ParticipantResponse {
        val user = participant.user
        val vaccination = participant.vaccination

        return ParticipantResponse(
            participant.id,
            ParticipantResponse.User(
                user.id,
                user.photo,
                user.name
            ),
            ParticipantResponse.Vaccination(
                vaccination.id,
                vaccination.title,
                vaccination.vaccine,
                vaccination.description,
                vaccination.picture,
                vaccination.startDate.time,
                vaccination.lastDate.time
            ),
            participant.status,
            participant.createdAt.time,
            participant.updatedAt.time
        )
    }
}