package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.domain.utils.safeLet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class
GetMoviesPairUseCase(
    private val repository: MoviesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override suspend fun execute(params: Unit): Result<Error, Pair<List<Movie>, List<Movie>>> {
        return coroutineScope {
            val movies = async(defaultDispatcher) {
                repository.getMovies().asSuccess()
            }
            val favoriteMovies = async(defaultDispatcher) {
                repository.getFavoriteMovies()
            }

            safeLet(movies.await(), favoriteMovies.await()) { moviesResult, favoriteMoviesResult ->
                Pair(moviesResult.data, favoriteMoviesResult)
            }?.let { Success(it) } ?: Failure(ServerError)
        }
    }
}
