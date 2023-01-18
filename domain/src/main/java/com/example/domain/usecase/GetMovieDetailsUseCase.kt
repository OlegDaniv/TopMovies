package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
    override val executor: ExecutorService,
    override val handler: HandlerWrapper
) : UseCase<String, MovieDetails>() {

    override fun execute(params: String): Result<Error, MovieDetails> =
        repository.getMovieDetails(params)
}
