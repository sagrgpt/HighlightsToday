package com.showcase.highlightstoday.di

import android.content.Context
import com.showcase.highlightstoday.repository.database.DatabaseFactory
import com.showcase.highlightstoday.repository.network.NetworkFactory
import com.showcase.highlightstoday.schedulers.DefaultScheduler
import com.showcase.highlightstoday.schedulers.SchedulerProvider

class CompositionRoot(
    val rootContext: Context
) {

    val scheduler: SchedulerProvider by lazy { DefaultScheduler() }
    val networkGateway by lazy {
        NetworkFactory.createGateway()
    }
    val databaseGateway by lazy {
        DatabaseFactory.createGateway(rootContext)
    }

}