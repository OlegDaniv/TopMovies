package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
) : UseCase<String, MovieDetails>() {

    override suspend fun execute(params: String): Result<Error, MovieDetails> = coroutineScope {
        withContext(Dispatchers.IO) {
            repository.getMovieDetails(params)
        }
    }
}
