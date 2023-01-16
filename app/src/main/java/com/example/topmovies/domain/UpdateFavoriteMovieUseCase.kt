package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MoviesRepository
import java.util.concurrent.ExecutorService

class UpdateFavoriteMovieUseCase(
    private val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<Params, Pair<List<Movie>, List<Movie>>>() {

    override fun run(params: Params): Result<Pair<List<Movie>, List<Movie>>> {
        repository.updateMovie(params.id, params.isFavorite)
        val movies = repository.getMovies().map { it.toMovie() }
        val favoriteMovies = repository.getFavoriteMovies(true).map { it.toMovie() }
        return Result(Pair(movies, favoriteMovies))
    }

    data class Params(val id: String, val isFavorite: Boolean)
}
