package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.repository.MoviesRepository
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result
import java.util.concurrent.ExecutorService

class LoadMoviesUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<Unit, List<Movie>>() {

    override fun execute(params: Unit): Result<Error, List<Movie>> = repository.loadNewMovies()
}
