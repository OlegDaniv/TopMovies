package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class LoadMoviesUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<None, List<Movie>>() {

    override fun run(params: None): Result<List<Movie>> {
        val data = repository.loadNewMovies()
        val movies = data.value.map { it.toMovie() }
        val error = data.error
        repository.upsertMoviesEntity(movies)
        return Result(movies, error)
    }
}
