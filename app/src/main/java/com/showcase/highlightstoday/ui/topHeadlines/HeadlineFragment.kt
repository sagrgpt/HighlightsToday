package com.showcase.highlightstoday.ui.topHeadlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.showcase.highlightstoday.DEFAULT_TAG
import com.showcase.highlightstoday.R
import com.showcase.highlightstoday.TAG_LIST
import com.showcase.highlightstoday.ViewEffects
import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.repository.database.DatabaseFactory
import com.showcase.highlightstoday.repository.network.NetworkFactory
import com.showcase.highlightstoday.schedulers.DefaultScheduler
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_headline.*

class HeadlineFragment : Fragment() {

    private lateinit var repository: NewsRepository
    private lateinit var adapter: ArticleAdapter
    private lateinit var viewModel: HeadlinesViewModel
    private lateinit var scheduler: SchedulerProvider
    private var disposable: Disposable? = null

    private val bottomListener: () -> Unit = {
        viewModel.viewMoreArticles(getSelectedTag())
    }

    private val clickListener: (String) -> Unit = {viewModel.articleClicked(it)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_headline, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewModel()
        initRecyclerView()
        initTags()
        setUpSwipeToRefresh()
        refreshWorkout(getSelectedTag())
    }

    override fun onResume() {
        super.onResume()
        observeSideEffects()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun trigger(effect: ViewEffects) {
        when (effect) {
            ViewEffects.RefreshCompleted -> swipeToRefresh.isRefreshing = false
            is ViewEffects.OpenNewsDetails -> navigateToDetailNews(effect.newsUrl)
        }
    }

    private fun refreshWorkout(tag: String) {
        swipeToRefresh.isRefreshing = true
        viewModel.viewArticles(tag)
    }

    private fun navigateToDetailNews(url: String) {
        val direction = HeadlineFragmentDirections
            .actionHeadlineFragmentToDetailNewsFragment(url)
        findNavController()
            .navigate(direction)
    }


    private fun initRecyclerView() {
        articleList?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = ArticleAdapter(requireContext(), bottomListener, clickListener)
        articleList?.adapter = adapter
    }

    private fun initTags() {
        for (index in TAG_LIST.indices) {
            val chip = layoutInflater.inflate(
                R.layout.tag_item,
                chipGroup,
                false
            ) as Chip
            chip.id = index + 1
            chip.text = TAG_LIST[index]
            chipGroup.addView(chip)
        }
        chipGroup.check(TAG_LIST.indexOf(DEFAULT_TAG) + 1)
        chipGroup.setOnCheckedChangeListener { _, id ->
            refreshWorkout(TAG_LIST[id - 1])
        }
    }

    private fun getSelectedTag() = TAG_LIST[chipGroup.checkedChipId - 1]

    private fun setUpSwipeToRefresh() {
        swipeToRefresh.setOnRefreshListener {
            refreshWorkout(getSelectedTag())
        }
    }

    private fun setUpViewModel() {
        val networkGateway = NetworkFactory.createGateway()
        val databaseGateway = DatabaseFactory.createGateway(requireContext().applicationContext)

        repository = NewsRepository(
            networkGateway.getNewsRemote(),
            databaseGateway.getNewsCache()
        )
        scheduler = DefaultScheduler()
        val viewModelFactory = HeadlinesViewModel.HeadlinesVmFactory(scheduler, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HeadlinesViewModel::class.java)

        viewModel.articles
            .observe(viewLifecycleOwner) { adapter.submitList(it) }

    }

    private fun observeSideEffects() {
        disposable = viewModel.listenViewEffects()
            .observeOn(scheduler.ui)
            .subscribe { trigger(it) }
    }
}