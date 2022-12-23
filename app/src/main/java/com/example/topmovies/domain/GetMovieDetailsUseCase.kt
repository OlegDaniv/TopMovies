package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.datalayer.repository.MovieDetailsRepository
import com.example.topmovies.datalayer.repository.MovieDetailsRepository.MovieDetails
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieDetailsRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<String, MovieDetails>() {

    override fun run(params: String): ResultOf<Failure, MovieDetails> =
        repository.getMovieDetails(params)
}
