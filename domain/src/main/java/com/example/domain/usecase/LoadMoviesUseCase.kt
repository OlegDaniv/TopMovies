package com.example.domain.usecase

import com.example.domain.models.Movie
import com.example.domain.repositores.MoviesRepository
import com.example.domain.utils.Error
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result
import java.util.concurrent.ExecutorService

class LoadMoviesUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: HandlerWrapper
) : UseCase<Unit, List<Movie>>() {

    override fun execute(params: Unit): Result<Error, List<Movie>> = repository.loadNewMovies()
}
