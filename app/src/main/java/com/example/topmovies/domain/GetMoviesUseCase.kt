package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.models.entity.toMovie
import com.example.topmovies.models.response.toMovie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class GetMoviesUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<None, Pair<List<Movie>, List<Movie>>>() {

    override fun run(params: None): Result<Pair<List<Movie>, List<Movie>>> {
        var movies = repository.getMoviesEntity().map { it.toMovie() }
        var error = ""
        movies.ifEmpty {
            val newMovie = repository.loadNewMovies()
            if (newMovie.error.isNotEmpty()) {
                error = newMovie.error
            } else {
                repository.upsertMoviesEntity(newMovie.value.map { it.toMovie() })
                movies = newMovie.value.map { it.toMovie() }
            }
        }
        val favoriteMovies = repository.getFavoriteMoviesEntity(true).map { it.toMovie() }
        return Result(Pair(movies, favoriteMovies), error)
    }

}
