package com.showcase.highlightstoday.ui.topHeadlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import kotlinx.android.synthetic.main.fragment_headline.*

class HeadlineFragment : Fragment() {

    private lateinit var repository: NewsRepository
    private lateinit var adapter: ArticleAdapter
    private lateinit var viewModel: HeadlinesViewModel

    private val bottomListener: () -> Unit = {
        viewModel.viewMoreArticles(getSelectedTag())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_headline, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewModel()
        initRecyclerView()
        initTags()
        setUpSwipeToRefresh()
    }

    override fun onResume() {
        super.onResume()
        refreshWorkout(getSelectedTag())
    }

    private fun trigger(effect: ViewEffects) {
        when (effect) {
            ViewEffects.RefreshCompleted -> swipeToRefresh.isRefreshing = false
        }
    }

    private fun refreshWorkout(tag: String) {
        swipeToRefresh.isRefreshing = true
        viewModel.viewArticles(tag)
    }

    private fun initRecyclerView() {
        articleList?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = ArticleAdapter(requireContext(), bottomListener)
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

        val viewModelFactory = HeadlinesViewModel.HeadlinesVmFactory(DefaultScheduler(), repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HeadlinesViewModel::class.java)

        viewModel.articles
            .observe(viewLifecycleOwner) { adapter.submitList(it) }


        viewModel.viewEffects
            .observe(viewLifecycleOwner) { trigger(it) }

    }
}