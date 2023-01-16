package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<String, MovieDetails>() {

    override fun run(params: String): Result<MovieDetails> {
        val detailsEntity = repository.getMovieDetailsEntityById(params)
        return if (detailsEntity == null) {
            val resultApi = repository.loadMovieDetails(params)
            if (resultApi.error.isNotEmpty()) {
                resultApi
            } else {
                repository.insertMovieDetailsEntity(resultApi.value)
                Result(resultApi.value)
            }
        } else {
            Result(detailsEntity)
        }
    }
}
