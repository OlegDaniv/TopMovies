package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MoviesRepository
import java.util.concurrent.ExecutorService

class GetMoviesUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<None, Pair<List<Movie>, List<Movie>>>() {

    override fun run(params: None): Result<Pair<List<Movie>, List<Movie>>> {
        var movies = repository.getMovies()
        var error = ""
        movies.ifEmpty {
            val newMovie = repository.loadNewMovies()
            if (newMovie.error.isNotEmpty()) {
                error = newMovie.error
            } else {
                repository.upsertMovies(newMovie.value)
                movies = newMovie.value
            }
        }
        val favoriteMovies = repository.getFavoriteMovies(true)
        return Result(Pair(movies, favoriteMovies), error)
    }

}
