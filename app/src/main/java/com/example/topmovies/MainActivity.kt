package com.example.topmovies

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topmovies.databinding.ActivityMainBinding
import com.example.topmovies.fragment.ToolbarBridge

class MainActivity : AppCompatActivity(), ToolbarBridge {
    
    private var isLoading = true
    private var mainMenu: Menu? = null
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { isLoading }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolBar()
        setupNavigationController()
        isLoading = false
    }
    
    override fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun hideBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        mainMenu = menu
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                true
            }
            R.id.setting -> {
                startSettingFragment()
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.takeIf { it.backStackEntryCount > 1 }
            ?.popBackStackImmediate()
            ?: super.onBackPressed()
    }
    

    private fun startSettingFragment() {
        Navigation.findNavController(this, R.id.fragment_main_activity)
            .navigate(R.id.setting_fragment)
    }

    private fun setupNavigationController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_top_movies, R.id.navigation_favorite_movies))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.buttonNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.setting_fragment -> hideBottomNav()
                else -> showBottomNav()
            }
            binding.buttonNavViewMainActivity.setupWithNavController(navController)
        }
    }

    private fun showBottomNav() {
        binding.buttonNavViewMainActivity.visibility = VISIBLE
        showSettingBottom()
    }

    private fun hideBottomNav() {
        binding.buttonNavViewMainActivity.visibility = GONE
        hideSettingBottom()
    }

    private fun showSettingBottom() {
        mainMenu?.findItem(R.id.setting)?.isVisible = true
    }

    private fun hideSettingBottom() {
        mainMenu?.findItem(R.id.setting)?.isVisible = false
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.toolBarMainActivity)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }
}
