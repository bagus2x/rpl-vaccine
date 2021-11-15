package com.group3.vaccinemaps.entity

import java.util.*
import javax.persistence.*

@Entity(name = "Vaccination")
class Vaccination(
    @Id
    @GeneratedValue
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val vaccine: String,

    val description: String?,

    @Column(columnDefinition = "VARCHAR(512)")
    val picture: String?,

    @Column(name = "start_date", nullable = false)
    val startDate: Date = Date(),

    @Column(name = "last_date", nullable = false)
    val lastDate: Date = Date(),

    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Date = Date(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "vaccination")
    val participants: List<Participant> = emptyList()
)