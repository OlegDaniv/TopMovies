package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class UpdateMovieUseCase(
    private val repository: MovieRepository,
    private val executor: ExecutorService,
    private val handler: Handler,
) {

    operator fun invoke(id: String, isFavorite: Boolean, onResult: (List<Movie>) -> Unit) {
        executor.execute {
            repository.updateMovie(id, isFavorite).let {
                val movies = repository.loadMovies()
                handler.post { onResult(movies.map { it.toMovie() }) }
            }
        }
    }
}
