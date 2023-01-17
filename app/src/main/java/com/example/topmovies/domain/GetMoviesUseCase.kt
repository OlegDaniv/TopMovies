package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.repository.MoviesRepository
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Failure
import com.example.topmovies.utils.Result.Success
import java.util.concurrent.ExecutorService

class GetMoviesUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override fun execute(params: Unit): Result<Error, Pair<List<Movie>, List<Movie>>> {
        val movies = repository.getMovies()
        return if (movies.isEmpty()) {
            when (val newMovie = repository.loadNewMovies()) {
                is Failure -> Failure(newMovie.error)
                is Success -> {
                    repository.upsertMovies(newMovie.result)
                    Success(Pair(newMovie.result, repository.getFavoriteMovies(true)))
                }
            }
        } else {
            Success(Pair(movies, repository.getFavoriteMovies(true)))
        }
    }
}
