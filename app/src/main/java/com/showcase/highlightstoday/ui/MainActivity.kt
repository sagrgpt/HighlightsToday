package com.showcase.highlightstoday.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.R
import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.repository.database.DatabaseFactory
import com.showcase.highlightstoday.repository.network.NetworkFactory
import com.showcase.highlightstoday.schedulers.DefaultScheduler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var repository: NewsRepository
    private lateinit var adapter: ArticleAdapter
    private lateinit var viewModel: HeadlinesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViewModel()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.viewArticles()
    }

    private fun initRecyclerView() {
        articleList?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        adapter = ArticleAdapter()
        articleList?.adapter = adapter
    }

    private fun setUpViewModel() {
        val networkGateway = NetworkFactory.createGateway()
        val databaseGateway = DatabaseFactory.createGateway(applicationContext)

        repository = NewsRepository(
            networkGateway.getNewsRemote(),
            databaseGateway.getNewsCache()
        )

        val viewModelFactory = HeadlinesViewModel.HeadlinesVmFactory(DefaultScheduler(), repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HeadlinesViewModel::class.java)

        viewModel.articles.observe(
            this,
            Observer<List<Article>> { adapter.submitList(it) }
        )
    }

}