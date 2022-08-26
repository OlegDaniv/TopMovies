package com.example.topmovies

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topmovies.databinding.ActivityMainBinding
import com.example.topmovies.fragment.ToolBarBridge


class MainActivity : AppCompatActivity(), ToolBarBridge {
    private var isLoading = true
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

    override fun showUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun hideUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                true
            }
            R.id.about -> {
                setToast()
                true
            }
            else -> false
        }
    }

    private fun setToast() {
        Toast.makeText(this, "the best app", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupNavigationController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_main_activity) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_top_movies, R.id.navigation_favorite_movies))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.buttonNavViewMainActivity.setupWithNavController(navController)
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.toolBarMainActivity)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
    }
}
