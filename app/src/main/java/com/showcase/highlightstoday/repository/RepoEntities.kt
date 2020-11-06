package com.showcase.highlightstoday.repository

/**
 * An exception wrapper for network module
 */
data class NetworkException(
    val code: Int,
    override val message: String
) : Exception(message)

data class ArticleEntity(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: Long,
    val source: SourceEntity = SourceEntity(),
    val title: String,
    val url: String,
    val urlToImage: String?,
    val isFavourite: Boolean = false,
    val category: String
)

data class SourceEntity(
    val id: String = "",
    val name: String = ""
)