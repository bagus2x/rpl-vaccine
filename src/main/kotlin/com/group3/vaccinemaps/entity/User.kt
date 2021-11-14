package com.group3.vaccinemaps.entity

import java.util.*
import javax.persistence.*

@Entity(name = "App_User")
class User(
    @Id
    @GeneratedValue
    val id: Long = 0,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "user")
    val participated: List<Participant> = emptyList(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: Set<Role> = emptySet(),

    @Column(columnDefinition = "VARCHAR(512)", nullable = true)
    val photo: String? = null,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = true, unique = true)
    val phoneNumber: String? = null,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = true, unique = true, columnDefinition = "VARCHAR(512)")
    val kk: String? = null,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: Date,

    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Date = Date(),
)