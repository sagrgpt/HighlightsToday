package com.showcase.highlightstoday.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.showcase.highlightstoday.R
import com.showcase.highlightstoday.di.CompositionRoot
import timber.log.Timber

class HostActivity : AppCompatActivity() {

    private lateinit var compositionRoot: CompositionRoot

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositionRoot = CompositionRoot(this)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_host)
        setUpNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
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