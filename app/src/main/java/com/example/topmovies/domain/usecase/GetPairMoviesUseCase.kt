package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.data.repository.MoviesRepository
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.presentation.models.Movie
import java.util.concurrent.ExecutorService

class GetPairMoviesUseCase(
    val repository: MoviesRepository,
    override val executor: ExecutorService,
    override val handler: Handler
) : UseCase<Unit, Pair<List<Movie>?, List<Movie>?>>() {

    override fun execute(params: Unit): ResultOf<Failure, Pair<List<Movie>?, List<Movie>?>> {
        val movies = repository.getMovies()
        val favoriteMovies = repository.getFavoriteMovies()
        val p = Pair(
            first = movies.getSuccess(),
            second = favoriteMovies.getSuccess()
        )
        return ResultOf.Success(p)
    }

}

