package com.showcase.highlightstoday.repository

import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.Source
import com.showcase.highlightstoday.repository.dataSource.NewsCache
import com.showcase.highlightstoday.repository.dataSource.NewsRemote
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber

class NewsRepository(
    private val remote: NewsRemote,
    private val cache: NewsCache
) {

    fun getHighlights(category: String, isFavourite: Boolean): Observable<List<Article>> {
        return if(isFavourite)
            cache.getFavourite(category)
                .map { it.toArticleList() }
        else cache.getHeadlines(category)
            .map {it.toArticleList()}
    }

    fun fetchArticlesFromRemote(category: String, isFavourite: Boolean): Completable {
        return Observable.fromCallable { (cache.getTotalArticleCount(category) /10) + 1 }
            .flatMapCompletable { fetchArticlesFromRemote(category, it, isFavourite) }
    }

    fun fetchArticlesFromRemote(category: String, pageNo: Int, isFavourite: Boolean = false): Completable {
        return if(isFavourite)
            Completable.complete()
        else remote.getHeadlines(category, pageNo)
            .map { cache.addArticles(it) }
            .ignoreElement()
    }

    fun addToFavourite(article: Article): Completable {
        return cache.addToFavourite(article.toEntity())
    }

    fun remoteFromFavourite(article: Article): Completable {
        return cache.removeFromFavourite(article.toEntity())
    }

    fun forceRefresh(category: String): Completable {
        return Observable.fromCallable{
            cache.clearCache()
        }
            .doOnNext { Timber.d("$it") }
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

    private fun Article.toEntity(): ArticleEntity {
        return ArticleEntity(
            title = title,
            source = SourceEntity(source.id, source.name),
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
