package com.zxltrxn.githubclient.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.Resource
import com.zxltrxn.githubclient.data.repository.IAuthRepository
import com.zxltrxn.githubclient.presentation.splash.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject lateinit var authRepo: IAuthRepository
    private val navController:NavController by lazy{ getNavigationController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        if(navController.currentDestination == navController.findDestination(R.id.splashFragment)) {
            authenticationWithRouting(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_sign_out){
            lifecycleScope.launch{
                authRepo.signOut()
            }
            navController.popBackStack(R.id.repositoriesListFragment,true)
            navController.navigate(R.id.authFragment)
        }
        return true
    }

    private fun getNavigationController(): NavController{
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    private fun authenticationWithRouting(navController: NavController){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                when(authRepo.signIn()){
                    is Resource.Success -> {
                        val action = SplashFragmentDirections
                            .splashFragmentToRepositoriesListFragment()
                        navController.navigate(action)
                    }
                    is Resource.Error ->{
                        val action = SplashFragmentDirections
                            .splashFragmentToAuthFragment()
                        navController.navigate(action)
                    }
                }
            }
        }
    }
}