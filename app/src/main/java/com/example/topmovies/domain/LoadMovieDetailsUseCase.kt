package com.example.topmovies.domain

import com.example.topmovies.models.MovieDetails
import com.example.topmovies.repository.MovieRepository

class LoadMovieDetailsUseCase(private val repository: MovieRepository) {

    operator fun invoke(
        data: String,
        onResult: (MovieDetails) -> Unit,
        onFailed: (String) -> Unit
    ) {
        repository.loadMovieDetails(data, {
            val movieDetails = it.toMovieDetails()
            onResult(movieDetails)
        }, {
            onFailed(it)
        })
    }
}