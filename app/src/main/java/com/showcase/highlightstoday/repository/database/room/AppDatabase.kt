package com.showcase.highlightstoday.repository.database.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleDbo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}