package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.MovieDetails
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
    private val executor: ExecutorService,
    private val handler: Handler,
    private val loadMovieDetailsUseCase: LoadMovieDetailsUseCase
) {

    operator fun invoke(id: String, onResult: (MovieDetails) -> Unit, onFailed: (String) -> Unit) {
        executor.execute {
            val result = repository.loadMovieDetailsById(id)?.toMovieDetails()
            result?.let { handler.post { onResult(it) } } ?: loadMovieDetailsUseCase(id, {
                handler.post { onResult(it) }
            }, {
                onFailed(it)
            })
        }
    }
}