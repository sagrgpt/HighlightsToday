package com.showcase.highlightstoday.repository.network

import com.google.gson.annotations.SerializedName

const val DEFAULT_COUNTRY = "in"
const val DEFAULT_CATEGORY = "technology"
const val DEFAULT_PAGE_SIZE = 10
const val DEFAULT_PAGE_NO = 1

data class NewsResponse(
    @SerializedName("articles")
    val articles: List<ArticleSchema> = listOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("totalResults")
    val totalResults: Int = 0,
    @SerializedName("code")
    val code: String = "ok",
    @SerializedName("message")
    val message: String = "Response successful"
)

data class ArticleSchema(
    @SerializedName("author")
    val author: String? = "Unknown",
    @SerializedName("content")
    val content: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("publishedAt")
    val publishedAt: String = "",
    @SerializedName("source")
    val source: SourceSchema = SourceSchema(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("urlToImage")
    val urlToImage: String? = ""
)

data class SourceSchema(
    @SerializedName("id")
    val id: String? = "Unknown",
    @SerializedName("name")
    val name: String = ""
)