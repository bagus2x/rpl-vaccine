package com.group3.vaccinemaps.entity

import javax.persistence.*

@Entity(name = "Role")
class Role(
    @Id
    @GeneratedValue
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    val name: ERole
)