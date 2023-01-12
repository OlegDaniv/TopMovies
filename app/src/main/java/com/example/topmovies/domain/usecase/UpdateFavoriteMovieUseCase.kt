package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Error.*
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.*
import com.example.topmovies.presentation.models.Movie
import java.util.concurrent.ExecutorService

class UpdateFavoriteMovieUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<Params, Pair<List<Movie>, List<Movie>>>() {

    override fun execute(params: Params): Result<Error, Pair<List<Movie>, List<Movie>>> {
        repository.updateMovie(params)
        val movies = repository.getMovies().asSuccess().result
        val favoriteMoves = repository.getFavoriteMovies().asSuccess().result
        val pair = let(movies, favoriteMoves) { movies, favoriteMovies ->
            Pair(movies, favoriteMovies)
        }
        return if (pair != null) {
            Success(pair)
        } else {
            Failure(ServerError)
        }
    }

    data class Params(val id: String, val isFavorite: Boolean)
}
