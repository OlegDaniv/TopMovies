package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.AppDispatchers
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import kotlinx.coroutines.withContext

class LoadMoviesUseCase(
    private val repository: MoviesRepository,
    private val dispatchers: AppDispatchers
) : UseCase<Unit, List<Movie>>() {

    override suspend fun execute(params: Unit): Result<Error, List<Movie>> =
        withContext(dispatchers.io) {
            repository.loadNewMovies()
        }
}
