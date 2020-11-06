package com.showcase.highlightstoday.repository

import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.Source
import com.showcase.highlightstoday.repository.dataSource.NewsCache
import com.showcase.highlightstoday.repository.dataSource.NewsRemote
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class NewsRepository(
    private val remote: NewsRemote,
    private val cache: NewsCache,
    private val scheduler: SchedulerProvider
) {

    private var disposable: Disposable? = null

    fun getHighlights(category: String): Observable<List<Article>> {
        return cache.getHeadlines(category)
            .map {it.toArticleList()}
    }


    fun fetchArticlesFromRemote(category: String): Completable {
        return Observable.fromCallable { cache.getTotalArticleCount(category) + 1 }
            .flatMap {
                remote.getHeadlines(category, it)
                    .toObservable()
            }
            .map { cache.addArticles(it) }
            .ignoreElements()
    }

    private fun List<ArticleEntity>.toArticleList(): List<Article> {
        val articleList = mutableListOf<Article>()
        forEach { articleList.add(it.toArticle()) }
        return articleList
    }

    private fun ArticleEntity.toArticle(): Article {
        return Article(
            title = title,
            source = Source(source.id, source.name),
            urlToImage = urlToImage,
            url = url,
            description = description,
            isFavourite = isFavourite,
            content = content,
            author = author,
            category = category,
            publishedAt = publishedAt
        )
    }
}