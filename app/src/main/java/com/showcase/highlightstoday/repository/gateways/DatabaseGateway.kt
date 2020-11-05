package com.showcase.highlightstoday.repository.gateways

import com.showcase.highlightstoday.repository.dataSource.NewsCache

interface DatabaseGateway {
    fun getNewsCache() : NewsCache
}