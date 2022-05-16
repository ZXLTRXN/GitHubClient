package com.zxltrxn.githubclient.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.presentation.auth.AuthFragmentDirections
import com.zxltrxn.githubclient.presentation.repositoriesList.RepositoriesListFragment
import com.zxltrxn.githubclient.presentation.repositoriesList.RepositoriesListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val auth by viewModels<AuthViewModel>()
    private val navController: NavController by lazy { getNavigationController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
//        val content: View = findViewById(android.R.id.content)
//        content.viewTreeObserver.addOnPreDrawListener(
//            object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    return if (auth.isAuthenticated()) {
//                        content.viewTreeObserver.removeOnPreDrawListener(this)
//                        true
//                    } else {
//                        navigateToAuth()
//                        false
//                    }
//                }
//            })
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                lifecycleScope.launch {
                    auth.signOut()
                }
                navController.popBackStack(R.id.repositoriesListFragment, true)
                navController.navigate(R.id.authFragment)
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getNavigationController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }


    private fun navigateToAuth() {
        val action = RepositoriesListFragmentDirections
            .repositoriesListFragmentToAuthFragment()
        navController.navigate(action)
    }
}