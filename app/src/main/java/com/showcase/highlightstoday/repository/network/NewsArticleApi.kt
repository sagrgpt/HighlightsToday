package com.showcase.highlightstoday.repository.network

import com.showcase.highlightstoday.API_KEY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * A retrofit interface that exposes the
 * [News APIs](https://newsapi.org/)
 */
interface NewsArticleApi {

    /**
     * Get a list of top headlines
     * @param country The 2-letter ISO 3166-1 code of the country you want to get headlines for.
     * @param category The category you want to get headlines for Eg: Business
     * @param pageSize The number of results to return per page.
     * @param pageNo Use this to page through the results.
     * @return List of news articles
     */
    @GET("v2/top-headlines")
    @Headers("Authorization: $API_KEY")
    fun getHeadlines(
        @Query("country") country: String = DEFAULT_COUNTRY,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("category") category: String,
        @Query("page") pageNo: Int
    ): Single<NewsResponse>

}