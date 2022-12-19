package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService


class GetMoviesUseCase(
    private val repository: MovieRepository,
    private val executor: ExecutorService,
    private val handler: Handler,
    private val loadMoviesUseCase: LoadMoviesUseCase,
) {

    operator fun invoke(onResult: (List<Movie>) -> Unit, onFailed: (String) -> Unit) {
        executor.execute {
            val result = repository.loadMovies().map { it.toMovie() }
            result.takeIf { it.isNotEmpty() }?.let {
                handler.post { onResult(result) }
            }
                ?: loadMoviesUseCase({ movieObject ->
//                    upsertMoviesUseCase(movieObject, {}, {})
                    onResult(movieObject.items.map { it.toMovie() })
                }, {
                    onFailed(it)
                })
        }
    }

}