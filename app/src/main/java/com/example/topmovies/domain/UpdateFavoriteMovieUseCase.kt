package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.repository.MoviesRepository
import com.example.topmovies.utils.Error
import com.example.topmovies.utils.Result
import com.example.topmovies.utils.Result.Success
import java.util.concurrent.ExecutorService

class UpdateFavoriteMovieUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<Params, Pair<List<Movie>, List<Movie>>>() {

    override fun execute(params: Params): Result<Error, Pair<List<Movie>, List<Movie>>> {
        repository.updateMovie(params.id, params.isFavorite)
        val movies = repository.getMovies()
        val favoriteMovies = repository.getFavoriteMovies(true)
        return Success(Pair(movies, favoriteMovies))
    }

    data class Params(val id: String, val isFavorite: Boolean)
}
