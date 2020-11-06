package com.showcase.highlightstoday.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.verify
import com.showcase.highlightstoday.repository.ArticleEntity
import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.repository.SourceEntity
import com.showcase.highlightstoday.repository.dataSource.NewsCache
import com.showcase.highlightstoday.repository.dataSource.NewsRemote
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HeadlinesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var cache: NewsCache
    @Mock
    private lateinit var remote: NewsRemote
    private val scheduler = object : SchedulerProvider {
        override val io: Scheduler
            get() = Schedulers.trampoline()
        override val ui: Scheduler
            get() = Schedulers.trampoline()
        override val computation: Scheduler
            get() = Schedulers.trampoline()
        override val newThread: Scheduler
            get() = Schedulers.trampoline()
    }
    private lateinit var repository: NewsRepository
    private lateinit var viewModel: HeadlinesViewModel

    private val article1 = ArticleEntity(
        author = "Shara Tibken",
        publishedAt = 1604646003000,
        category = "technology",
        title = "iPhone 12 Pro, iOS 14.2 let people who are blind detect others around them - CNET",
        content = "The lidar scanner on Apple's new iPhone 12 Pro and 12 Pro Max enables new AR features -- and the ability for people who are blind or low vision to detect others around them.\\r\\nJames Martin/CNET\\r\\nApple… [+5575 chars]",
        isFavourite = false,
        url = "https://www.cnet.com/news/iphone-12-pro-lets-people-who-are-blind-detect-others-around-them/",
        urlToImage = "https://www.cnet.com/news/iphone-12-pro-lets-people-who-are-blind-detect-others-around-them/",
        description = "A new People Detection features uses lidar to alert iPhone 12 Pro, 12 Pro Max and iPad Pro users how close other people are.",
        source = SourceEntity("Unknown", "CNET")
    )

    private val article2 = article1.copy(title = "iPhone 12 Pro", publishedAt = 1607896003000)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = NewsRepository(remote, cache)
        viewModel = HeadlinesViewModel(scheduler, repository)
//        viewModel.liveData.observeForever(mockObserver)
    }

    @Test
    fun connectionTest() {
        val cachedArticleList = listOf(article1, article1, article1, article1, article1)
        val category = "technology"
        `when`(cache.getHeadlines(category))
            .thenReturn(Observable.just(cachedArticleList))

        viewModel.viewArticles(category)

        val result = viewModel.articles.value!!
        assert(result.size == 5)
    }

    @Test
    fun emptyCacheTest() {
        val cachedArticleList = emptyList<ArticleEntity>()
        val remoteArticleList = listOf(article1,article1)
        val category = "technology"
        var emitter: ObservableEmitter<List<ArticleEntity>>? = null
        `when`(cache.getHeadlines(category))
            .thenReturn(Observable.create {
                emitter = it
                it.onNext(cachedArticleList)
            })

        `when`(cache.getTotalArticleCount(category))
            .thenReturn(0)
        `when`(remote.getHeadlines(category, 1))
            .thenReturn(Single.just(remoteArticleList))

        viewModel.viewArticles(category)
        emitter?.onNext(remoteArticleList)

        verify(remote).getHeadlines(category,1)
        val result = viewModel.articles.value!!
        assert(result.size ==2)
    }

    @Test
    fun remotePaginationTest(){
        val cachedArticleList = mutableListOf(
            article1,article1,article1,article1,article1,article1,article1,article1,article1,article1
        )
        val remoteArticleList = listOf(article1,article1)
        val category = "technology"
        val subject: PublishSubject<List<ArticleEntity>> = PublishSubject.create()

        `when`(cache.getHeadlines(category))
            .thenReturn(subject)
        `when`(cache.getTotalArticleCount(category))
            .thenReturn(cachedArticleList.size)
        `when`(remote.getHeadlines(category, 2))
            .thenReturn(Single.just(remoteArticleList))

        viewModel.viewArticles(category)
        subject.onNext(cachedArticleList)
        viewModel.viewMoreArticles(category)
        cachedArticleList.addAll(remoteArticleList)
        subject.onNext(cachedArticleList)

        verify(remote).getHeadlines(category, 2)
        val result = viewModel.articles.value!!
        assert(result.size == 12)

    }
}