package com.group3.vaccinemaps.entity

import java.util.*
import javax.persistence.*

@Entity
class Notification(

    @Id
    @GeneratedValue
    val id: Long = 0,

    @Column(columnDefinition = "VARCHAR(512)")
    val picture: String? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    val receiver: User,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "VARCHAR(512)")
    val content: String? = null,

    @Enumerated(EnumType.STRING)
    var status: ENotificationStatus,

    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date()
)