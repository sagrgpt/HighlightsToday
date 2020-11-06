package com.showcase.highlightstoday.repository.database

import android.content.Context
import androidx.room.Room
import com.showcase.highlightstoday.repository.gateways.DatabaseGateway
import com.showcase.highlightstoday.repository.database.room.AppDatabase

object DatabaseFactory {

    private const val dbName = "NewsDb"
    private var gateway: DatabaseGateway? = null

    fun createGateway(context: Context): DatabaseGateway {
        return gateway
            ?: CacheGateway(buildDatabase(context.applicationContext))
                .also { gateway = it }
    }

    private fun buildDatabase(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            dbName
        ).build()
    }
}