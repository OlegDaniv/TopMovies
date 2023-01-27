package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.domain.utils.safeLet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class GetMoviesPairUseCase(
    private val repository: MoviesRepository,
) : UseCase<Unit, Pair<List<Movie>, List<Movie>>>() {

    override suspend fun execute(params: Unit): Result<Error, Pair<List<Movie>, List<Movie>>> =
        coroutineScope() {
            val movies = withContext(Dispatchers.IO) {
                repository.getMovies().asSuccess()
            }
            val favoriteMovies =
                withContext(Dispatchers.IO) { repository.getFavoriteMovies() }
            safeLet(movies, favoriteMovies) { moviesResult, favoriteMoviesResult ->
                Pair(moviesResult.data, favoriteMoviesResult)
            }?.let { Success(it) } ?: Failure(ServerError)
        }
}
