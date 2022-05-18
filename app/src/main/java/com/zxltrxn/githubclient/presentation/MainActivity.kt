package com.zxltrxn.githubclient.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.presentation.AuthViewModel.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val auth by viewModels<AuthViewModel>()
    private val navController: NavController by lazy { getNavigationController() }

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setPreDrawListener()
    }

    private fun setPreDrawListener() {
        var destination: Int = R.id.repositoriesListFragment
        if (auth.state is State.NotReady) {
            auth.trySignInWithSaved()
            val content: View = findViewById(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        return if (auth.state !is State.NotReady) {
                            if (auth.state is State.NotAuthenticated) {
                                navigateToAuth()
//                                destination = R.id.authFragment
                            }
//                            setStartDestination(destination)
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            false
                        }
                    }
                }
            )
        }
    }

//    private fun setStartDestination(destination: Int) {
//        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
//        navGraph.setStartDestination(R.id.repositoriesListFragment)
//        navController.graph = navGraph
//    }

    private fun getNavigationController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    private fun navigateToAuth() {
        navController.navigate(R.id.to_AuthFragment)
    }
}