package com.group3.vaccinemaps.entity.id

import java.io.Serializable
import javax.persistence.Column

data class ParticipantID(
    @Column(
        name = "user_id",
        nullable = false
    )
    val user: Long,

    @Column(
        name = "vaccination_id",
        nullable = false
    )
    val vaccination: Long
) : Serializable