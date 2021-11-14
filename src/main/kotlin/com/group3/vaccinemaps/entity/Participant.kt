package com.group3.vaccinemaps.entity

import java.util.*
import javax.persistence.*

@Entity(name = "Participant")
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "vaccination_id"])])
class Participant(
    @Id
    @GeneratedValue
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_id", nullable = false, referencedColumnName = "id")
    val vaccination: Vaccination,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: EParticipantStatus = EParticipantStatus.WAITING,

    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Date = Date(),
)