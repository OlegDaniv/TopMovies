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

    private val movieAdapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerview.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = movieAdapter
            }
        }
        val movieViewModel =
            ViewModelProvider(this, ModelFactory(MovieRepository()))[MovieViewModel::class.java]
                .apply {
                    movieList.observe(this@MainActivity) {
                        movieAdapter.apply {
                            setMovieList(it)
                            notifyItemRangeChanged(0, it.size)
                        }
                    }
                }
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                movieViewModel.getAllMovies()
                isRefreshing = false
            }
        }
    }
}