package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.GetMoviesUseCase.Result
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService


class GetMoviesUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<None, Result<List<Movie>>>() {

    override fun run(params: None): Data<Result<List<Movie>>> {
        var movies = repository.getMoviesEntity().map { it.toMovie() }
        var error = ""
        movies.ifEmpty {
            val newMovie = repository.loadNewMovies()
            if (newMovie.error.isNotEmpty()) {
                error = newMovie.error
                newMovie
            } else {
                repository.upsertMoviesEntity(newMovie.data.map { it.toMovie() })
                movies = newMovie.data.map { it.toMovie() }
            }
        }
        val favoriteMovies = repository.getFavoriteMoviesEntity(true).map { it.toMovie() }
        return Data(Result(movies, favoriteMovies), error)
    }

    data class Result<T>(val movies: T, val favorite: T)
}
