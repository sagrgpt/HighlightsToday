package com.showcase.highlightstoday.repository.database.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "articles_table",
    primaryKeys = ["title", "published_at"]
)
data class ArticleDbo(
    val title: String,
    @ColumnInfo(name = "published_at")
    val publishedAt: Long,
    val category: String,
    val content: String,
    val description: String,
    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean,
    val url: String,
    @ColumnInfo(name = "image_url")
    val urlToImage: String,
    val author: String,
    @Embedded
    val source: SourceDbo

)

data class SourceDbo(
    @ColumnInfo(name = "source_id")
    val id: String = "",
    @ColumnInfo(name = "source_name")
    val name: String = ""
)