package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.repository.MovieDetailsRepository
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<String, MovieDetails>() {

    override fun execute(params: String): Result<Error, MovieDetails> =
        repository.getMovieDetails(params)
}
