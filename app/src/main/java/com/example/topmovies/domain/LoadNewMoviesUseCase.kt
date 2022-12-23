package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.datalayer.repository.MoviesRepository
import com.example.topmovies.datalayer.repository.MoviesRepository.Movie
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf
import java.util.concurrent.ExecutorService

class LoadNewMoviesUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<None, List<Movie>>() {

    override fun run(params: None): ResultOf<Failure, List<Movie>> = repository.loadNewMovie()
}
