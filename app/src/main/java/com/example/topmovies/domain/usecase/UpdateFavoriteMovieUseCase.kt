package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.presentation.models.Movie
import java.util.concurrent.ExecutorService

class UpdateFavoriteMovieUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<Params, Pair<List<Movie>?, List<Movie>?>>() {

    override fun execute(params: Params): ResultOf<Failure, Pair<List<Movie>?, List<Movie>?>> {
        repository.updateMovie(params)
        val movies = repository.getMovies()
        val favoriteMovies = repository.getFavoriteMovies()
        val p = Pair(
            first = movies.getSuccess(),
            second = favoriteMovies.getSuccess()
        )
        return ResultOf.Success(p)
    }

    data class Params(val id: String, val isFavorite: Boolean)
}
