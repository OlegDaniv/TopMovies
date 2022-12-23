package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.datalayer.repository.MoviesRepository
import com.example.topmovies.datalayer.repository.MoviesRepository.Movie
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf
import java.util.concurrent.ExecutorService

class UpdateFavoriteMovieUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<Params, List<Movie>>() {

    override fun run(params: Params): ResultOf<Failure, List<Movie>> {
        repository.updateMovieEntity(params)
        return repository.getMoviesEntity()
    }

    data class Params(val id: String, val isFavorite: Boolean)
}
