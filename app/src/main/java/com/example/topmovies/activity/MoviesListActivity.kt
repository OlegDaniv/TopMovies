package com.example.topmovies.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.ActivityListMoviesBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.INTENT_KEY
import com.example.topmovies.viewmodel.MovieListViewModel
import com.example.topmovies.viewmodel.MovieModelFactory

class MoviesListActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener {

    private val movieListAdapter by lazy { MovieAdapter(this) }
    private val movieListViewModel by lazy {
        ViewModelProvider(
            this, MovieModelFactory(MovieRepository())
        )[MovieListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupViewModel()
    }

    override fun onClickItem(movieId: String) {
        openDetailedActivity(movieId)
    }

    private fun setupViewModel() {
        movieListViewModel.movieList.observe(this) {
            movieListAdapter.apply {
                setMovieList(it)
                notifyItemRangeChanged(0, it.size)
            }
        }
    }

    private fun setupUI() {
        ActivityListMoviesBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = movieListAdapter
            }
            swipeRefresh.setOnRefreshListener {
                movieListViewModel.getAllMovies()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun openDetailedActivity(movieId: String) {
        Intent(this, MovieDetailedActivity()::class.java).apply {
            putExtra(INTENT_KEY, movieId)
            startActivity(this)
        }
    }
}