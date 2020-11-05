package com.showcase.highlightstoday.repository.network

import com.showcase.highlightstoday.repository.NetworkGateway
import com.showcase.highlightstoday.repository.dataSource.NewsRemote


class RemoteGateway(
    private val newsApi: NewsApi
) : NetworkGateway {

    override fun getNewsRemote(): NewsRemote {
        return NewsService(newsApi)
    }

}