package com.showcase.highlightstoday.ui.detailNews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.showcase.highlightstoday.R
import kotlinx.android.synthetic.main.detail_news_fragment.*

class DetailNewsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailNewsFragment()
    }

    private lateinit var viewModel: DetailNewsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.detail_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailNewsViewModel::class.java)
        newsWebView.loadUrl("https://www.gadgetsnow.com/tech-news/apple-brings-100-new-emoji-and-more-to-iphones-ipads/articleshow/79093832.cms")
    }

}