package com.showcase.highlightstoday.repository.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticles(article: ArticleDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllArticles(articleList: List<ArticleDbo>)

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY published_at DESC")
    fun getHeadlines(category: String): Observable<List<ArticleDbo>>

    @Query("SELECT * FROM articles_table WHERE category = :category AND is_saved = :isSaved ORDER BY published_at DESC")
    fun getSaved(category: String, isSaved: Boolean = true): Observable<List<ArticleDbo>>

    @Query("UPDATE articles_table SET is_saved = :isSaved WHERE title = :title AND published_at = :publishedAt")
    fun toggleFavourite(isSaved: Boolean, title: String, publishedAt: Long)

    @Query("DELETE FROM articles_table WHERE category = :category AND is_saved = :isSaved  ")
    fun deleteArticles(category: String, isSaved: Boolean = false)

    @Query("SELECT COUNT(title) FROM articles_table WHERE category = :category")
    fun getArticleCountFor(category: String): Int

}