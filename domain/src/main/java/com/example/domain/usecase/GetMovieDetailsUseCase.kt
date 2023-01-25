package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
) : UseCase<String, MovieDetails>() {

    override suspend fun execute(params: String): Result<Error, MovieDetails> =
        repository.getMovieDetails(params)
}
