package com.showcase.highlightstoday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
    }

}