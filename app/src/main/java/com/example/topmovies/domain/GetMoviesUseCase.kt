package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class GetMoviesUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<None, Pair<List<Movie>, List<Movie>>>() {

    override fun run(params: None): Result<Pair<List<Movie>, List<Movie>>> {
        var movies = repository.getMoviesEntity()
        var error = ""
        movies.ifEmpty {
            val newMovie = repository.loadNewMovies()
            if (newMovie.error.isNotEmpty()) {
                error = newMovie.error
            } else {
                repository.upsertMoviesEntity(newMovie.value)
                movies = newMovie.value
            }
        }
        val favoriteMovies = repository.getFavoriteMoviesEntity(true)
        return Result(Pair(movies, favoriteMovies), error)
    }

}
