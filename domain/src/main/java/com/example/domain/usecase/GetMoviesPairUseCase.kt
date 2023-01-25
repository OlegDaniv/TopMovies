package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.domain.utils.safeLet

class GetMoviesPairUseCase(
    private val repository: MoviesRepository,
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override suspend fun execute(params: Unit): Result<Error, Pair<List<Movie>, List<Movie>>> {
        val movies = repository.getMovies().asSuccess()
        val favoriteMovies = repository.getFavoriteMovies()
        return safeLet(movies, favoriteMovies) { moviesResult, favoriteMoviesResult ->
            Pair(moviesResult.data, favoriteMoviesResult)
        }?.let { Success(it) } ?: Failure(ServerError)
    }
}
