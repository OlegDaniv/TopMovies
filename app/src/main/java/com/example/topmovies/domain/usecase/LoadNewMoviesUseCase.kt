package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.presentation.models.Movie
import java.util.concurrent.ExecutorService

class LoadNewMoviesUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<Unit, List<Movie>>() {

    override fun execute(params: Unit): Result<Error, List<Movie>> = repository.loadNewMovie()
}
