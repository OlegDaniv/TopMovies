package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.None
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.presentation.models.Movie
import java.util.concurrent.ExecutorService

class GetFavoriteMovieUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<None, List<Movie>>() {

    override fun run(params: None): ResultOf<Failure, List<Movie>> =
        repository.getFavoriteMovies()
}
