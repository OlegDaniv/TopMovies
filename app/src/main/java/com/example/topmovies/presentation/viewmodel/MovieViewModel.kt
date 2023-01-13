package com.example.topmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.usecase.GetMoviesPairUseCase
import com.example.topmovies.domain.usecase.LoadNewMoviesUseCase
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase
import com.example.topmovies.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Failure
import com.example.topmovies.domain.utils.Result.Success
import com.example.topmovies.presentation.models.Movie
import com.example.topmovies.presentation.utils.EnumScreen
import com.example.topmovies.presentation.utils.EnumScreen.FAVORITE
import com.example.topmovies.presentation.utils.EnumScreen.MOVIES

class MovieViewModel constructor(
    private val getMoviesPair: GetMoviesPairUseCase,
    private val loadNewMovies: LoadNewMoviesUseCase,
    private val updateFavoriteMovie: UpdateFavoriteMovieUseCase
) : BaseViewModel() {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()

    fun getObservableList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            MOVIES -> movies
            FAVORITE -> favoriteMovies
        }
    }

    fun getMovies() {
        getMoviesPair(Unit) {
            handleResult(it)
        }
    }

    fun loadNewMovies() {
        loadNewMovies(Unit) { result ->
            result.process(
                { handleError(it) },
                { handleMovies(it) }
            )
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            MOVIES -> switchMovieToFavorites(id, !favorite)
            FAVORITE -> switchMovieToFavorites(id, false)
        }
    }

    private fun switchMovieToFavorites(id: String, isFavorite: Boolean) {
        updateFavoriteMovie(Params(id, isFavorite)) {
            handleResult(it)
        }
    }

    private fun handleResult(result: Result<Error, Pair<List<Movie>, List<Movie>>>) {
        when (result) {
            is Failure -> handleError(result.error)
            is Success -> {
                handleMovies(result.result.first)
                handleFavoriteMovies(result.result.second)
            }
        }
    }

    private fun handleFavoriteMovies(newFavoriteMovies: List<Movie>) {
        favoriteMovies.value = newFavoriteMovies
    }

    private fun handleMovies(newMovie: List<Movie>) {
        movies.value = newMovie
    }
}
