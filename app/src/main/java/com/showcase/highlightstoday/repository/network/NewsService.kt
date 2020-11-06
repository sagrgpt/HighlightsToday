package com.showcase.highlightstoday.repository.network

import com.showcase.highlightstoday.repository.ArticleEntity
import com.showcase.highlightstoday.repository.NetworkException
import com.showcase.highlightstoday.repository.SourceEntity
import com.showcase.highlightstoday.repository.dataSource.NewsRemote
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*

/**
 * This class converts the json values from server to network entity
 * @param api: The service needed for api communication
 * @see [NewsApi]
 */
class NewsService(
    private val api: NewsApi
) : NewsRemote {

    override fun getHeadlines(category: String, page: Int): Single<List<ArticleEntity>> {
         return api.getHeadlines(category = category, pageNo = page)
             .map {
                 if(it.status == it.code)
                    it.toArticleList(category)
                 else throw NetworkException(404, "Failed to get Headlines. ${it.message}")
             }
    }

    private fun NewsResponse.toArticleList(category: String): List<ArticleEntity> {
        val articleList = mutableListOf<ArticleEntity>()
        articles.forEach {
            articleList.add(it.toArticleEntity(category))
        }
        return articleList
    }

    private fun ArticleSchema.toArticleEntity(category: String): ArticleEntity{
        return ArticleEntity(
            author = author ?: "Unknown",
            content = content ?: "Unknown",
            description = description ?: "Unknown",
            publishedAt = getEpochFrom(publishedAt),
            source = source.toEntity(),
            title = title,
            url = url,
            urlToImage = urlToImage ?: "",
            category = category
        )
    }

    private fun SourceSchema.toEntity(): SourceEntity {
        return SourceEntity(
            id = id ?: "Unknown",
            name = name
        )
    }

    fun getEpochFrom(publishedAt: String): Long {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT)
            .parse(publishedAt.replace("Z","+0000" ))
            ?.time
            ?: 0
    }

}