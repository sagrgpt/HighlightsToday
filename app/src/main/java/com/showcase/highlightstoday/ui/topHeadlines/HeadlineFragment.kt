package com.showcase.highlightstoday.ui.topHeadlines

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.showcase.highlightstoday.DEFAULT_TAG
import com.showcase.highlightstoday.R
import com.showcase.highlightstoday.TAG_LIST
import com.showcase.highlightstoday.schedulers.SchedulerProvider
import com.showcase.highlightstoday.ui.BaseFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_headline.*

class HeadlineFragment : BaseFragment() {

    private lateinit var viewModel: HeadlinesViewModel
    private var disposable: Disposable? = null
    lateinit var viewModelFactory: HeadlinesViewModel.HeadlinesVmFactory
    lateinit var scheduler: SchedulerProvider
    lateinit var adapter: ArticleAdapter
    val bottomListener: () -> Unit = {
        viewModel.viewMoreArticles(getSelectedTag())
    }
    val clickListener: (ClickEvent) -> Unit = {
        viewModel.onClick(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInjector().inject(this)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favouriteFilter -> handleFavoriteFilter(item)
            R.id.clearCache -> viewModel.onClick(ClickEvent.ClearCache)
        }
        return super.onOptionsItemSelected(item)
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
            ViewEffects.NetworkError -> Toast.makeText(
                context,
                getString(R.string.internetIssue),
                Toast.LENGTH_LONG).show()
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

    private fun handleFavoriteFilter(item: MenuItem) {
        if (item.title == getString(R.string.view_favorite)) {
            item.title = getString(R.string.view_all)
            item.setIcon(R.drawable.ic_favorite_selected)
        } else {
            item.title = getString(R.string.view_favorite)
            item.setIcon(R.drawable.ic_favorite_unselected)
        }
        viewModel.onClick(ClickEvent.ToggleFavouriteFilter)
    }

    private fun initRecyclerView() {
        articleList?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
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
        swipeToRefresh.setColorSchemeResources(R.color.design_default_color_primary)
        swipeToRefresh.setOnRefreshListener {
            refreshWorkout(getSelectedTag())
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(HeadlinesViewModel::class.java)
        viewModel.articles
            .observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun observeSideEffects() {
        disposable = viewModel.listenViewEffects()
            .observeOn(scheduler.ui)
            .subscribe { trigger(it) }
    }
}