package com.group3.vaccinemaps.services

import com.group3.vaccinemaps.entity.Article
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.payload.Validation
import com.group3.vaccinemaps.payload.request.CreateArticleRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response.ArticleResponse
import com.group3.vaccinemaps.repository.ArticleRepository
import com.group3.vaccinemaps.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val validation: Validation
) {

    fun create(req: CreateArticleRequest): ArticleResponse {
        validation.validate(req)

        val author = userRepository.findByIdOrNull(req.authorId) ?: throw NotFoundException("Author not found")
        val article = Article(
            picture = req.picture,
            title = req.title!!,
            content = req.content!!,
            author = author
        )

        articleRepository.save(article)

        return mapArticleToResponse(article)
    }

    fun getById(articleId: Long): ArticleResponse {
        val article = articleRepository.findByIdOrNull(articleId) ?: throw NotFoundException("Article not found")

        return mapArticleToResponse(article)
    }

    fun list(req: PaginationRequest): List<ArticleResponse> {
        val page = articleRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(req.page, req.size))

        return page.fold(mutableListOf()) { accumulator, item -> accumulator.add(mapArticleToResponse(item)); accumulator }
    }

    fun delete(articleId: Long) {
        val article = articleRepository.findByIdOrNull(articleId) ?: throw NotFoundException("Article not found")

        articleRepository.deleteById(article.id)
    }

    private fun mapArticleToResponse(article: Article): ArticleResponse {
        val author = article.author

        return ArticleResponse(
            article.id,
            article.picture,
            article.title,
            article.content,
            article.createdAt.time,
            article.updatedAt.time,
            ArticleResponse.Author(
                author.id,
                author.photo,
                author.name
            )
        )
    }
}