package com.example.topmovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.ActivityMainBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.viewmodel.ModelFactory
import com.example.topmovies.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private val movieAdapter by lazy { MovieAdapter() }

    private val movieViewModel by lazy {
        ViewModelProvider(
            this,
            ModelFactory(MovieRepository())
        )[MovieViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupViewModel()
    }

    private fun setupViewModel() {
        movieViewModel.movieList.observe(this@MainActivity) {
            movieAdapter.apply {
                setMovieList(it)
                notifyItemRangeChanged(0, it.size)
            }
        }
    }

    private fun setupUI() {
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerview.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = movieAdapter
            }
            swipeRefresh.setOnRefreshListener {
                movieViewModel.getAllMovies()
                swipeRefresh.isRefreshing = false
            }
        }
    }
}