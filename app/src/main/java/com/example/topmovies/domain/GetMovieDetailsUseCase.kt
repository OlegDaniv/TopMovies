package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.models.MovieDetails
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<String, MovieDetails>() {

    override fun run(params: String): Data<MovieDetails> {
        val result = repository.getMovieDetailsEntityById(params)
        return if (result == null) {
            val data = repository.loadMovieDetails(params)
            if (data.error.isNotEmpty()) {
                data
            } else {
                repository.insertMovieDetailsEntity(data.data.toMovieDetailsEntity())
                Data(data.data)
            }
        } else {
            Data(result.toMovieDetails())
        }
    }
}
