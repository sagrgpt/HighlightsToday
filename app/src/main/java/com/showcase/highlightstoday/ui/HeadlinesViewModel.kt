package com.showcase.highlightstoday.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.DEFAULT_TAG
import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class HeadlinesViewModel(
    private val scheduler: SchedulerProvider,
    private val repository: NewsRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val articleLiveData = MutableLiveData<List<Article>>()
    private var selectedTag: String = DEFAULT_TAG
    val articles: LiveData<List<Article>> = articleLiveData

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun viewArticles(tag: String = selectedTag) {
        selectedTag = tag
        disposable.add(
            repository.getHighlights(selectedTag)
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::readList, ::handleError)
        )
    }

    fun viewMoreArticles(tag: String = selectedTag) {
        selectedTag = tag
        disposable.add(
            repository.fetchArticlesFromRemote(tag)
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::handleSuccess, ::handleError)
        )
    }

    private fun handleSuccess() {
        Timber.v("Fetched new workouts from remote")
    }

    private fun handleError(e: Throwable) {
        Timber.e(e)
    }

    private fun readList(articleList: List<Article>) {
        if (articleList.isEmpty())
            viewMoreArticles()
        else
            articleLiveData.postValue(articleList)
    }

    class HeadlinesVmFactory(
        private val scheduler: SchedulerProvider,
        private val repository: NewsRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HeadlinesViewModel(scheduler, repository) as T
        }
    }
}
