package com.group3.vaccinemaps.repository

import com.group3.vaccinemaps.entity.EParticipantStatus
import com.group3.vaccinemaps.entity.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository : JpaRepository<Participant, Long> {

    fun findByUserIdAndVaccinationId(userId: Long, vaccinationId: Long): Participant?

    fun findByIdAndUserId(participantId: Long, userId: Long): Participant?

    fun findByUserId(userId: Long): List<Participant>

    fun countByUserIdAndStatus(userId: Long, status: EParticipantStatus): Int
}