package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService


class GetFavoriteMovieUseCase(
    private val repository: MovieRepository,
    private val executorService: ExecutorService,
    private val handler: Handler,
) {

    operator fun invoke(onResult: (List<Movie>) -> Unit) {
        executorService.execute {
            val movies = repository.loadFavoriteMovie(true).map { it.toMovie() }
            handler.post { onResult(movies) }
        }
    }
}
