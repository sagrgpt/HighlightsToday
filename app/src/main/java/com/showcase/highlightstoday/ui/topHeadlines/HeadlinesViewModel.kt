package com.showcase.highlightstoday.ui.topHeadlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.DEFAULT_TAG
import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.IOException

class HeadlinesViewModel(
    private val scheduler: SchedulerProvider,
    private val repository: NewsRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val articleLiveData = MutableLiveData<List<Article>>()
    private val viewEffectsLiveData: PublishSubject<ViewEffects> = PublishSubject.create()
    private var selectedTag: String = DEFAULT_TAG
    private var isFavourite = false
    val articles: LiveData<List<Article>> = articleLiveData

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun listenViewEffects(): Observable<ViewEffects> {
        return viewEffectsLiveData
    }

    fun viewArticles(tag: String) {
        disposable.clear()
        selectedTag = tag
        disposable.addAll(
            repository.getHighlights(selectedTag, isFavourite)
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::readList, ::handleError),
            repository.fetchArticlesFromRemote(selectedTag, 1, isFavourite)
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::handleSuccess, ::handleError)
        )
    }

    fun viewMoreArticles(tag: String = selectedTag) {
        selectedTag = tag
        disposable.add(
            repository.fetchArticlesFromRemote(tag, isFavourite)
                .retry()
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::handleSuccess, ::handleError)
        )
    }

    fun onClick(clickEvent: ClickEvent) {
        when (clickEvent) {
            is ClickEvent.CardClicked -> viewEffectsLiveData.onNext(
                ViewEffects.OpenNewsDetails(clickEvent.articleLink)
            )
            is ClickEvent.AddToFavourite -> addToFavourite(clickEvent.article)
            is ClickEvent.RemoteFavourite -> removeFromFavourite(clickEvent.article)
            ClickEvent.ToggleFavouriteFilter -> {
                isFavourite = !isFavourite
                viewArticles(selectedTag)
            }
            ClickEvent.ClearCache -> hardRefresh()
        }

    }

    private fun addToFavourite(article: Article) {
        disposable.add(
            repository.addToFavourite(article)
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::handleSuccess, ::handleError)
        )
    }

    private fun removeFromFavourite(article: Article) {
        disposable.add(
            repository.remoteFromFavourite(article)
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::handleSuccess, ::handleError)
        )
    }

    private fun hardRefresh() {
        disposable.add(
            repository.forceRefresh()
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.io)
                .subscribe(::handleSuccess, ::handleError)
        )
    }

    private fun handleSuccess() {
        viewEffectsLiveData.onNext(ViewEffects.RefreshCompleted)
        Timber.v("Fetched new workouts from remote")
    }

    private fun handleError(e: Throwable) {
        if (e is IOException)
            viewEffectsLiveData.onNext(ViewEffects.NetworkError)
        viewEffectsLiveData.onNext(ViewEffects.RefreshCompleted)
        Timber.e(e)
    }

    private fun readList(articleList: List<Article>) {
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
