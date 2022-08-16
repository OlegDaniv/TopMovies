package com.example.topmovies.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.topmovies.databinding.ActivityDetailsMovieBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.INTENT_KEY
import com.example.topmovies.viewmodel.MovieDetailsViewModel
import com.example.topmovies.viewmodel.MovieModelFactory

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsMovieBinding
    private val movieDetailsViewModel by lazy {
        ViewModelProvider(
            this, MovieModelFactory(MovieRepository())
        )[MovieDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMovieDetailsById()
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            movieDetailsViewModel.movieDetails.observe(this@MovieDetailsActivity) {
                textviewMovieDetailsDescription.text = it.plot
                Glide.with(imageviewMovieDetailsImage).asBitmap().load(it.imageUrl)
                    .into(imageviewMovieDetailsImage)
                textviewMovieDetailsMovieTitle.text = it.title
                textviewMovieDetailsRatingNumber.text = it.imDbRating
                textviewMovieDetailsGenresSource.text = it.genres
                textviewMovieDetailsDateReleaseSource.text = it.releaseDate
            }
        }
    }

    private fun loadMovieDetailsById() {
        intent.extras?.apply {
            if (get(INTENT_KEY) != null) {
                getString(INTENT_KEY)?.let {
                    movieDetailsViewModel.getMovieDetails(it)
                }
            }
        }
    }
}
