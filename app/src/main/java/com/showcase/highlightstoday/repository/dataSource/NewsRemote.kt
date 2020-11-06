package com.showcase.highlightstoday.repository.dataSource

import com.showcase.highlightstoday.repository.ArticleEntity
import com.showcase.highlightstoday.repository.network.DEFAULT_PAGE_NO
import io.reactivex.Single

/**
 * An entry point to source news articles
 */
interface NewsRemote {

    /**
     * Get a list of latest headlines.
     * @param category The category you want to get headlines for Eg: Business
     * @param page Use this to page through the results.
     * @return List of news articles
     */
    fun getHeadlines(
        category: String,
        page: Int = DEFAULT_PAGE_NO
    ): Single<List<ArticleEntity>>

}