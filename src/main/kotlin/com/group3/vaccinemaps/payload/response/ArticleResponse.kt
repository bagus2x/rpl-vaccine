package com.group3.vaccinemaps.payload.response

data class ArticleResponse(
    val id: Long,
    val picture: String?,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val author: Author
) {

    data class Author(
        val id: Long,
        val photo: String?,
        val name: String
    )
}