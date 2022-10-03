package com.example.topmovies

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topmovies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private var isLoading = true
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { isLoading }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationController()
        isLoading = false
    }
    
    override fun onSupportNavigateUp() =
        findNavController(R.id.fragment_container).navigateUp(appBarConfiguration)
    
    private fun setupNavigationController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_top_movies, R.id.navigation_favorite_movies, R.id.navigation_setting
            )
        )
        binding.bottomNavView.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_movie_details -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.GONE
                }
                else -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }
}
