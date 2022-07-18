package com.example.topmovies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topmovies.adapter.MovieAdapter
import com.example.topmovies.databinding.ActivityMainBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.retrofit.ApiServer
import com.example.topmovies.viewmodel.ModelFactory
import com.example.topmovies.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private val retrofitService = ApiServer.getInstance()
    private val adapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this, ModelFactory(MovieRepository(retrofitService))
        )[MovieViewModel::class.java]
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
        viewModel.movieList.observe(this) { adapter.setMovieList(it) }
        viewModel.errorMessage.observe(this) {}
        viewModel.getAllMovies()
    }
}