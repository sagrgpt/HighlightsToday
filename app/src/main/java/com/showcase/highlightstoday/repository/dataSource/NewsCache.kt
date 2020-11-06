package com.showcase.highlightstoday.repository.dataSource

import com.showcase.highlightstoday.repository.ArticleEntity
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * An entry point to source cached news articles
 */
interface NewsCache {

    fun addArticles(articleList: List<ArticleEntity>)

    fun getTotalArticleCount(category: String): Int

    fun getHeadlines(
        category: String
    ): Observable<List<ArticleEntity>>

    fun getFavourite(
        category: String
    ): Observable<List<ArticleEntity>>

    fun removeFromFavourite(articleEntity: ArticleEntity): Completable

    fun addToFavourite(articleEntity: ArticleEntity): Completable

}