package com.example.domain.usecase

import com.example.domain.models.MovieDetails
import com.example.domain.repositores.MovieDetailsRepository
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UseCase<String, MovieDetails>() {

    override suspend fun execute(params: String): Result<Error, MovieDetails> = coroutineScope {
        async(defaultDispatcher) {

        }
//        withContext(defaultDispatcher) {
            repository.getMovieDetails(params)
//        }
    }
}
