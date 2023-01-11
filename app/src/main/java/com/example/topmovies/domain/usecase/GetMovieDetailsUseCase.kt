package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MovieDetailsRepository
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.presentation.models.MovieDetails
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<String, MovieDetails>() {

    override fun execute(params: String): Result<Failure, MovieDetails> =
        repository.getMovieDetails(params)
}
