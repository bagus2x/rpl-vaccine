package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.services.ParticipantService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class ParticipantController(private val participantService: ParticipantService) {


}