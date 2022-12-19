package com.example.topmovies.domain

import com.example.topmovies.models.Movie
import com.example.topmovies.models.MovieObject
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class UpsertMoviesUseCase(
    private val repository: MovieRepository,
    private val executorService: ExecutorService,
    private val getMoviesUseCase: GetMoviesUseCase
) {

    operator fun invoke(
        data: MovieObject,
        onResult: (List<Movie>) -> Unit,
        onFailed: (String) -> Unit
    ) {
        executorService.execute {
            repository.upsertMovies(data.items.map { it.toMovie() })
            getMoviesUseCase({
                onResult(it)
            }, {
                onFailed(it)
            })

        }
    }
}