package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.domain.utils.safeLet

class UpdateFavoriteMovieUseCase(
    private val repository: MoviesRepository,
) : UseCase<Params, Pair<List<Movie>, List<Movie>>>() {

    override suspend fun execute(params: Params): Result<Error, Pair<List<Movie>, List<Movie>>> {
        repository.updateMovie(params.id, params.isFavorite)
        val movies = repository.getMovies().asSuccess()
        val favoriteMovies = repository.getFavoriteMovies()
        return safeLet(movies, favoriteMovies) { moviesResult, favoriteMoviesResult ->
            Pair(moviesResult.data, favoriteMoviesResult)
        }?.let { Success(it) } ?: Failure(ServerError)
    }
}

data class Params(val id: String, val isFavorite: Boolean)
