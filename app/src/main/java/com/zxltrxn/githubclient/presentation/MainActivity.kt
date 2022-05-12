package com.zxltrxn.githubclient.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.network.APIService.Companion.WRONG_TOKEN_CODE
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.presentation.splash.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    val IS_ENTERED_KEY = "isEntered"

    @Inject
    lateinit var authRepo: IAuthRepository
    private val navController: NavController by lazy { getNavigationController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val isEntered = savedInstanceState?.getBoolean(IS_ENTERED_KEY)
        if (isEntered == null || isEntered == false) authenticationWithRouting()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (navController.currentDestination != navController.findDestination(R.id.splashFragment)) {
            outState.putBoolean(IS_ENTERED_KEY, true)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                lifecycleScope.launch {
                    authRepo.signOut()
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

    private fun authenticationWithRouting() {
        lifecycleScope.launch {
            when (val res = authRepo.signIn()) {
                is Resource.Success -> {
                    navigateToRepositoriesList()
                }
                is Resource.Error -> {
                    if (res.code == WRONG_TOKEN_CODE) {
                        navigateToAuth()
                    } else {
                        navigateToRepositoriesList()
                    }
                }
            }
        }
    }

    private fun navigateToRepositoriesList() {
        val action = SplashFragmentDirections
            .splashFragmentToRepositoriesListFragment()
        navController.navigate(action)
    }

    private fun navigateToAuth() {
        val action = SplashFragmentDirections
            .splashFragmentToAuthFragment()
        navController.navigate(action)
    }
}