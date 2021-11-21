package com.group3.vaccinemaps.repository

import com.group3.vaccinemaps.entity.Article
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {

    fun findAllByOrderByCreatedAtDesc(page: Pageable): List<Article>
}