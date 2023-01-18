package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.repository.MoviesRepository
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Error.ServerError
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Failure
import com.example.topmovies.utils.Result.Success
import com.example.topmovies.utils.safeLet
import java.util.concurrent.ExecutorService

class GetMoviesPairUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override fun execute(params: Unit): Result<Error, Pair<List<Movie>, List<Movie>>> {
        val movies = repository.getMovies().asSuccess()
        val favoriteMovies = repository.getFavoriteMovies()
        return safeLet(movies, favoriteMovies) { moviesResult, favoriteMoviesResult ->
            Pair(moviesResult.result, favoriteMoviesResult)
        }?.let { Success(it) } ?: Failure(ServerError)
    }
}
