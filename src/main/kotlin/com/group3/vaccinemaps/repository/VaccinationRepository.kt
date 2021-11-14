package com.group3.vaccinemaps.repository

import com.group3.vaccinemaps.entity.Vaccination
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VaccinationRepository : JpaRepository<Vaccination, Long>