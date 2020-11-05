package com.showcase.highlightstoday.repository.database

import com.showcase.highlightstoday.repository.ArticleEntity
import com.showcase.highlightstoday.repository.SourceEntity
import com.showcase.highlightstoday.repository.dataSource.NewsCache
import com.showcase.highlightstoday.repository.database.room.ArticleDao
import com.showcase.highlightstoday.repository.database.room.ArticleDbo
import io.reactivex.Completable
import io.reactivex.Observable

class NewsDb(
    private val dao: ArticleDao
) : NewsCache {

    override fun getTotalArticleCount(category: String): Int {
        return dao.getArticleCountFor(category)
    }

    override fun getHeadlines(category: String): Observable<List<ArticleEntity>> {
        return dao.getHeadlines(category)
            .map { it.toEntityList() }
    }

    override fun getFavourite(category: String): Observable<List<ArticleEntity>> {
        return dao.getSaved(category)
            .map { it.toEntityList() }
    }

    override fun removeFromFavourite(articleEntity: ArticleEntity): Completable {
        return Completable.fromCallable {
            dao.toggleFavourite(false, articleEntity.title, articleEntity.publishedAt)
        }
    }

    override fun addToFavourite(articleEntity: ArticleEntity): Completable {
        return Completable.fromCallable {
            dao.toggleFavourite(true, articleEntity.title, articleEntity.publishedAt)
        }
    }

    private fun List<ArticleDbo>.toEntityList(): List<ArticleEntity> {
        val entityList = mutableListOf<ArticleEntity>()
        forEach { entityList.add(it.toEntity()) }
        return entityList
    }

    private fun ArticleDbo.toEntity(): ArticleEntity {
        return ArticleEntity(
            title = title,
            publishedAt = publishedAt,
            category = category,
            author = author,
            content = content,
            isFavourite = isSaved,
            description = description,
            url = url,
            urlToImage = urlToImage,
            source = SourceEntity(source.id, source.name)
        )
    }
}
