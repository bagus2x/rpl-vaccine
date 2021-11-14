package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.EParticipantStatus
import com.group3.vaccinemaps.entity.Participant
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.exception.UnprocessableException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.CreateParticipantRequest
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

    fun register(req: CreateParticipantRequest): ParticipantResponse {
        validation.validate(req)

        var participant = participantRepository.findByUserIdAndVaccinationId(
            req.userId!!,
            req.vaccinationId!!
        )
        if (participant != null) throw UnprocessableException("Already registered. Current status is '${participant.status}'")

        val accepted = participantRepository.countByUserIdAndStatus(req.userId, EParticipantStatus.ACCEPTED)
        if (accepted >= 2) throw UnprocessableException("Maximum limit of 'ACCEPTED status' has been exceeded")

        val waiting = participantRepository.countByUserIdAndStatus(req.userId, EParticipantStatus.WAITING)
        if (waiting >= 2) throw UnprocessableException("Maximum limit of 'WAITING status' has been exceeded")

        val vcn = vaccinationRepository.findByIdOrNull(req.vaccinationId) ?: throw NotFoundException("Vaccination not found")
        if (vcn.lastDate.time >= System.currentTimeMillis()) throw UnprocessableException("Registration closed")

        val user = userRepository.findByIdOrNull(req.userId) ?: throw NotFoundException("User not found")

        participant = Participant(
            user = user,
            vaccination = vcn,
            status = EParticipantStatus.WAITING
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

    fun listByUserId(userId: Long): List<ParticipantResponse> {
        val participants = participantRepository.findByUserId(userId)

        return participants.map { mapParticipantToResponse(it) }
    }

    fun accept(participantId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdOrNull(participantId) ?: throw NotFoundException("Participant not found")
        participant.status = EParticipantStatus.ACCEPTED
        participantRepository.save(participant)

        return mapParticipantToResponse(participant)
    }

    fun reject(participantId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdOrNull(participantId) ?: throw NotFoundException("Participant not found")
        participant.status = EParticipantStatus.REJECTED
        participantRepository.save(participant)

        return mapParticipantToResponse(participant)
    }

    fun cancel(participantId: Long, userId: Long): ParticipantResponse {
        val participant = participantRepository.findByIdOrNull(participantId) ?: throw NotFoundException("Participant not found")
        if (participant.user.id != userId) throw AccessDeniedException("Access denied")

        participant.status = EParticipantStatus.CANCELED
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