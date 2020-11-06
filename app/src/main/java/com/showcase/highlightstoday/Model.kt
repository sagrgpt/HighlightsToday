package com.showcase.highlightstoday

import com.showcase.highlightstoday.repository.SourceEntity

data class Article(
    val title: String,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: Long,
    val isFavourite: Boolean = false,
    val category: String,
    val url: String,
    val urlToImage: String,
    val source: Source = Source()
)

data class Source(
    val id: String = "",
    val name: String = ""
)