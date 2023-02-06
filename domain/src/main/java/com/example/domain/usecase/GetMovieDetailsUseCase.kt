package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.AppDispatchers
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import kotlinx.coroutines.withContext

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
    private val dispatchers: AppDispatchers
) : UseCase<String, MovieDetails>() {

    override suspend fun execute(params: String): Result<Error, MovieDetails> =
        withContext(dispatchers.io) {
            repository.getMovieDetails(params)
        }
}
