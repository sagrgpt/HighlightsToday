package com.showcase.highlightstoday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.showcase.highlightstoday.DEFAULT_TAG
import com.showcase.highlightstoday.R
import com.showcase.highlightstoday.TAG_LIST
import com.showcase.highlightstoday.ViewEffects
import com.showcase.highlightstoday.di.CompositionRoot
import com.showcase.highlightstoday.repository.NewsRepository
import com.showcase.highlightstoday.repository.database.DatabaseFactory
import com.showcase.highlightstoday.repository.network.NetworkFactory
import com.showcase.highlightstoday.schedulers.DefaultScheduler
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : AppCompatActivity() {

    private lateinit var compositionRoot: CompositionRoot

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositionRoot = CompositionRoot(this)
        setContentView(R.layout.activity_host)
        setUpNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        return  navController.navigateUp()
    }

    fun getCompositionRoot(): CompositionRoot {
        return compositionRoot
    }

    private fun setUpNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}