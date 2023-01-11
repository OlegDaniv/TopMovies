package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Failure.ServerError
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Error
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.Movie
import java.util.concurrent.ExecutorService

class GetPairMoviesUseCase(
    val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override fun execute(params: Unit): Result<Failure, Pair<List<Movie>, List<Movie>>> {

        val movies = repository.getMovies().asSuccess().result
        val favoriteMoves = repository.getFavoriteMovies().asSuccess().result
        val pair = let(movies, favoriteMoves) { movies, favoriteMovies ->
            Pair(movies, favoriteMovies)
        }
        return if (pair != null) {
            Success(pair)
        } else {
            Error(ServerError)
        }
    }
}
