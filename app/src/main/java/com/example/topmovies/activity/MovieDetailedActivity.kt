package com.example.topmovies.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.topmovies.databinding.ActivityDetailedReviewMovieBinding
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.INTENT_KEY
import com.example.topmovies.viewmodel.MovieDetailedViewModel
import com.example.topmovies.viewmodel.MovieModelFactory

class MovieDetailedActivity : AppCompatActivity() {

    private val movieDetailedViewModel by lazy {
        ViewModelProvider(
            this, MovieModelFactory(MovieRepository())
        )[MovieDetailedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMovieInfo()
        setupUI()
    }

    private fun setupUI() {
        ActivityDetailedReviewMovieBinding.inflate(layoutInflater).apply {
            setContentView(root)
            movieDetailedViewModel.movieDetailed.observe(this@MovieDetailedActivity) {
                movieDetailedDescriptionText.text = it.plot
                Glide.with(movieDetailedImage).asBitmap().load(it.imageUrl).into(movieDetailedImage)
                movieDetailedMovieTitle.text = it.title
                movieDetailedRatingNumber.text = it.imDbRating
                movieDetailedGenresSource.text = it.genres
                movieDetailedDateReleaseSource.text = it.releaseDate
            }
        }
    }

    private fun getMovieInfo() {
        intent.extras?.apply {
            if (get(INTENT_KEY) != null) {
                getString(INTENT_KEY)?.let {
                    movieDetailedViewModel.getMovieDetailed(it)
                }
            }
        }
    }
}