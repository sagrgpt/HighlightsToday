package com.showcase.highlightstoday.repository.database

import com.showcase.highlightstoday.repository.gateways.DatabaseGateway
import com.showcase.highlightstoday.repository.dataSource.NewsCache
import com.showcase.highlightstoday.repository.database.room.AppDatabase

/**
 * A single point of entrance to the
 * database module. It exposes all the api that
 * the repository layer expects the storage layer to host.
 */
class CacheGateway(
    private val appDatabase: AppDatabase
): DatabaseGateway {
    override fun getNewsCache(): NewsCache {
        return NewsDb(appDatabase.getArticleDao())
    }
}