package com.showcase.highlightstoday.di

import android.content.Context
import com.showcase.highlightstoday.repository.database.DatabaseFactory
import com.showcase.highlightstoday.repository.network.NetworkFactory
import com.showcase.highlightstoday.schedulers.DefaultScheduler
import com.showcase.highlightstoday.schedulers.SchedulerProvider

/**
 * Top level component in the dependency graph.
 * This component is attached to the top most android framework component in use.
 * Currently, this root is attached to Activity
 * @see [PresentationRoot]
 */
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