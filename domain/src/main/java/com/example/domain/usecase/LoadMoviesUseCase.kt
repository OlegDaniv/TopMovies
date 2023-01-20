package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class LoadMoviesUseCase(
    private val repository: MoviesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UseCase<Unit, List<Movie>>() {

    override suspend fun execute(params: Unit): Result<Error, List<Movie>> = coroutineScope {
        withContext(defaultDispatcher) {
            repository.loadNewMovies()
        }
    }
}
