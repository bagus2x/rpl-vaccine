package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.payload.Payload
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.request.CreateArticleRequest
import com.group3.vaccinemaps.payload.request.DeleteArticleRequest
import com.group3.vaccinemaps.payload.request.PaginationRequest
import com.group3.vaccinemaps.payload.response.ArticleResponse
import com.group3.vaccinemaps.services.ArticleService
import com.group3.vaccinemaps.utils.user
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class ArticleController(private val articleService: ArticleService) {

    @PostMapping(
        value = ["/article"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun create(authentication: Authentication, @RequestBody req: CreateArticleRequest): WebResponse<ArticleResponse> {
        val author = authentication.user
        val article = articleService.create(req.copy(authorId = author.id))

        return WebResponse.ok(Payload(201, "Created", article))
    }

    @GetMapping(
        value = ["/article/{articleId}"],
        produces = ["application/json"]
    )
    fun getById(@PathVariable articleId: String): WebResponse<ArticleResponse> {
        val aId = articleId.toLongOrNull() ?: throw BadRequestException("Invalid article id")
        val article = articleService.getById(aId)

        return WebResponse.ok(Payload(200, "Ok", article))
    }

    @GetMapping(
        value = ["/articles"]
    )
    fun list(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): WebResponse<List<ArticleResponse>> {
        val req = PaginationRequest(page, size)
        val articles = articleService.list(req)

        return WebResponse.ok(Payload(200, "Ok", articles))
    }

    @DeleteMapping(
        value = ["/article/{articleId}"],
        produces = ["application/json"]
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(authentication: Authentication, @PathVariable articleId: String): WebResponse<Long> {
        val aId = articleId.toLongOrNull() ?: throw BadRequestException("Invalid article id")
        val author = authentication.user
        val req = DeleteArticleRequest(aId, author.id)

        articleService.delete(req)

        return WebResponse.ok(Payload(200, "Ok", aId))
    }
}