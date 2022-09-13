package com.example.topmovies

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topmovies.databinding.ActivityMainBinding
import com.example.topmovies.fragment.AboutDialogFragment
import com.example.topmovies.fragment.ToolbarBridge

class MainActivity : AppCompatActivity(), ToolbarBridge {
    
    private var isLoading = true
    private lateinit var binding: ActivityMainBinding
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            supportFragmentManager.takeIf { it.backStackEntryCount > 1 }
                ?.popBackStackImmediate()
                ?: onBackPressedDispatcher.onBackPressed()
        }
    }
    
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
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedCallback.handleOnBackPressed()
                true
            }
            R.id.about -> {
                showDialog()
                true
            }
            else -> false
        }
    }
    
    private fun showDialog() {
        AboutDialogFragment().show(
            supportFragmentManager,
            getString(R.string.main_activity_dialog_tag)
        )
    }
    
    private fun setupNavigationController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_top_movies, R.id.navigation_favorite_movies))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.toolBarMainActivity)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }
}
