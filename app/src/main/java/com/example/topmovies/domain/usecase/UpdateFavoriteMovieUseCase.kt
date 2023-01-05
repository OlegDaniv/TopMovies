package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.presentation.models.Movie
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
