package com.group3.vaccinemaps.entity

import java.util.*
import javax.persistence.*

@Entity(name = "Article")
class Article(
    @Id
    @GeneratedValue
    val id: Long = 0,

    @Column(columnDefinition = "VARCHAR(512)")
    val picture: String?,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Date = Date(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    val author: User
)
