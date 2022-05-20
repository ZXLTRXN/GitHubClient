package com.zxltrxn.githubclient.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.storage.KeyValueStorage
import com.zxltrxn.githubclient.presentation.AuthViewModel.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject
    lateinit var storage: KeyValueStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val navController = getNavigationController()
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (savedInstanceState == null) {
            val destinationId = if (storage.authToken != null) {
                R.id.repositoriesListFragment
            } else {
                R.id.authFragment
            }
            navGraph.setStartDestination(destinationId)
        }
        navController.graph = navGraph
    }

    private fun getNavigationController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}