package com.showcase.highlightstoday.repository.database.room

import androidx.room.*
import io.reactivex.Observable

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addArticles(article: ArticleDbo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllArticles(articleList: List<ArticleDbo>)

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY published_at DESC")
    fun getHeadlines(category: String): Observable<List<ArticleDbo>>

    @Query("SELECT * FROM articles_table WHERE category = :category AND is_saved = :isSaved ORDER BY published_at DESC")
    fun getSaved(category: String, isSaved: Boolean = true): Observable<List<ArticleDbo>>

    @Query("UPDATE articles_table SET is_saved = :isSaved WHERE title = :title AND published_at = :publishedAt AND category = :category")
    fun toggleFavourite(isSaved: Boolean, title: String, publishedAt: Long, category: String)

    @Query("DELETE FROM articles_table")
    fun deleteArticles(): Int

    @Query("SELECT COUNT(title) FROM articles_table WHERE category = :category")
    fun getArticleCountFor(category: String): Int

}