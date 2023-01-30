package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.AppDispatchers
import com.example.domain.utils.Error
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.domain.utils.safeLet
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetMoviesPairUseCase(
    private val repository: MoviesRepository,
    private val dispatchers: AppDispatchers
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override suspend fun execute(params: Unit): Result<Error, Pair<List<Movie>, List<Movie>>> =
        withContext(dispatchers.default) {
            val movies = async(dispatchers.io) { repository.getMovies().asSuccess() }
            val favoriteMovies = async(dispatchers.io) { repository.getFavoriteMovies() }
            return@withContext safeLet(
                movies.await(), favoriteMovies.await()
            ) { moviesResult, favoriteMoviesResult ->
                Pair(moviesResult.data, favoriteMoviesResult)
            }?.let { Success(it) } ?: Failure(ServerError)
        }
}
