package com.example.topmovies.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.ActivityMoviesBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.INTENT_KEY
import com.example.topmovies.viewmodel.MovieModelFactory
import com.example.topmovies.viewmodel.MoviesViewModel

class MoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviesBinding
    private val moviesAdapter by lazy { MovieAdapter { id -> onClickItem(id) } }
    private val moviesViewModel by lazy {
        ViewModelProvider(
            this, MovieModelFactory(MovieRepository())
        )[MoviesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupViewModel()
    }

    private fun onClickItem(movieId: String) {
        startMovieDetailsActivity(movieId)
    }

    private fun setupViewModel() {
        moviesViewModel.movies.observe(this) {
            moviesAdapter.apply {
                setMovieList(it)
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            recyclerviewMoviesActivity.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = moviesAdapter
            }
            swipeRefresh.setOnRefreshListener {
                moviesViewModel.getAllMovies()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun startMovieDetailsActivity(movieId: String) {
        Intent(this, MovieDetailsActivity()::class.java).apply {
            putExtra(INTENT_KEY, movieId)
            startActivity(this)
        }
    }
}
