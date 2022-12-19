package com.example.topmovies.domain

import com.example.topmovies.models.MovieObject
import com.example.topmovies.repository.MovieRepository

class LoadMoviesUseCase(private val repository: MovieRepository) {

    operator fun invoke(onResult: (MovieObject) -> Unit, onFailed: (String) -> Unit) {
        repository.loadNewMovies(
            onSuccess = { onResult(it) },
            onError = { onFailed(it) }
        )
    }
}