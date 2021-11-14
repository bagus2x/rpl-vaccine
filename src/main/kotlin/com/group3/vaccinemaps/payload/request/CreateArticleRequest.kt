package com.group3.vaccinemaps.payload.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateArticleRequest(
    val picture: String?,
    val authorId: Long,
    @field:NotBlank
    @field:NotNull
    @field:Size(min = 5, max = 128)
    val title: String?,
    @field:NotBlank
    @field:NotNull
    val content: String?
)