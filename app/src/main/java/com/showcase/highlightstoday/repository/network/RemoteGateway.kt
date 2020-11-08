package com.showcase.highlightstoday.repository.network

import com.showcase.highlightstoday.repository.gateways.NetworkGateway
import com.showcase.highlightstoday.repository.dataSource.NewsRemote

class RemoteGateway(
    newsApi: NewsArticleApi
) : NetworkGateway {

    private val newsRemote by lazy { NewsService(newsApi) }

    override fun getNewsRemote(): NewsRemote {
        return newsRemote
    }

}