package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.domain.GetMoviesUseCase.Result
import com.example.topmovies.domain.UpdateMovieUseCase.Params
import com.example.topmovies.models.Movie
import com.example.topmovies.repository.MovieRepository
import java.util.concurrent.ExecutorService

class UpdateMovieUseCase(
    private val repository: MovieRepository,
    override val executor: ExecutorService,
    override val handler: Handler,
) : UseCase<Params, Result<List<Movie>>>() {

    override fun run(params: Params): Data<Result<List<Movie>>> {
        repository.updateMovieEntity(params.id, params.isFavorite)
        val movies = repository.getMoviesEntity().map { it.toMovie() }
        val favoriteMovies = repository.getFavoriteMoviesEntity(true).map { it.toMovie() }
        return Data(Result(movies, favoriteMovies))
    }

    data class Params(val id: String, val isFavorite: Boolean)
}
