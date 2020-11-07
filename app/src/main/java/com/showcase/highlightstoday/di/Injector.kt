package com.showcase.highlightstoday.di

import com.showcase.highlightstoday.ui.detailNews.DetailNewsFragment
import com.showcase.highlightstoday.ui.topHeadlines.HeadlineFragment
import timber.log.Timber
import java.lang.RuntimeException

class Injector(
    private val presentationRoot: PresentationRoot
) {

    fun inject(client: Any) {
        when (client) {
            is HeadlineFragment -> injectDependencies(client)
            is DetailNewsFragment -> injectDependencies(client)
            else -> throw RuntimeException("Invalid view injection")
        }
    }

    private fun injectDependencies(client: HeadlineFragment) {
        val repository = presentationRoot.getNewsRepository(
            presentationRoot.getNewsRemote(),
            presentationRoot.getNewsCache()
        )
        client.scheduler = presentationRoot.getScheduler()
        client.viewModelFactory = presentationRoot.getHeadlineViewModelFactory(
            presentationRoot.getScheduler(),
            repository
        )
        client.adapter = presentationRoot.getArticleAdapter(
            client.bottomListener,
            client.clickListener
        )
    }

    private fun injectDependencies(client: DetailNewsFragment) {
        Timber.i("$client has no external dependency")
    }
}