package com.showcase.highlightstoday.di

import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.repository.dataSource.NewsCache
import com.showcase.highlightstoday.repository.dataSource.NewsRemote
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import com.showcase.highlightstoday.ui.topHeadlines.ArticleAdapter
import com.showcase.highlightstoday.ui.topHeadlines.ClickEvent
import com.showcase.highlightstoday.ui.topHeadlines.HeadlinesViewModel

/**
 * Second level component in the dependency graph.
 * This root is synonymous to presentation layer.
 * Currently, this is attached to every fragment
 * @see [CompositionRoot]
 */
class PresentationRoot(
    private val compositionRoot: CompositionRoot
) {

    fun getArticleAdapter(
        bottomListener: () -> Unit,
        clickListener: (ClickEvent) -> Unit
    ): ArticleAdapter {
        return ArticleAdapter(
            compositionRoot.rootContext,
            bottomListener,
            clickListener
        )
    }

    fun getScheduler(): SchedulerProvider {
        return compositionRoot.scheduler
    }

    fun getHeadlineViewModelFactory(
        scheduler: SchedulerProvider,
        repository: NewsRepository
    ): HeadlinesViewModel.HeadlinesVmFactory {
        return HeadlinesViewModel.HeadlinesVmFactory(
            scheduler,
            repository
        )
    }

    fun getNewsRepository(
        remote: NewsRemote,
        cache: NewsCache
    ): NewsRepository {
        return NewsRepository(
            remote,
            cache
        )
    }

    fun getNewsCache(): NewsCache {
        return compositionRoot
            .databaseGateway
            .getNewsCache()
    }

    fun getNewsRemote(): NewsRemote {
        return compositionRoot
            .networkGateway
            .getNewsRemote()
    }
}