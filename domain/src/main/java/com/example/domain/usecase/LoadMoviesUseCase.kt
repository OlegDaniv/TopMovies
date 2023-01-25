package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result

class LoadMoviesUseCase(
    private val repository: MoviesRepository,
) : UseCase<Unit, List<Movie>>() {

    override suspend fun execute(params: Unit): Result<Error, List<Movie>> =
        repository.loadNewMovies()
}
