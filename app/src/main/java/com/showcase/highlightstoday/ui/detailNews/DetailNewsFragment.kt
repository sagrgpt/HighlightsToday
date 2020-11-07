package com.showcase.highlightstoday.ui.detailNews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.showcase.highlightstoday.R
import com.showcase.highlightstoday.ui.BaseFragment
import kotlinx.android.synthetic.main.detail_news_fragment.*

class DetailNewsFragment : BaseFragment() {

    private val args: DetailNewsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInjector().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.detail_news_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = args.url
        newsWebView.webViewClient = WebViewClient()
        newsWebView.loadUrl(url)
    }

}