package com.example.topmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.models.Movie
import com.example.domain.usecase.GetMoviesPairUseCase
import com.example.domain.usecase.LoadMoviesUseCase
import com.example.domain.usecase.UpdateFavoriteMovieUseCase
import com.example.domain.usecase.UpdateFavoriteMovieUseCase.Params
import com.example.domain.utils.Error
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.topmovies.presentation.utils.EnumScreen
import com.example.topmovies.presentation.utils.EnumScreen.FAVORITE
import com.example.topmovies.presentation.utils.EnumScreen.MOVIES

class MovieViewModel constructor(
    private val getMoviesPair: GetMoviesPairUseCase,
    private val loadMovies: LoadMoviesUseCase,
    private val updateMovie: UpdateFavoriteMovieUseCase,
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
            handlePairResult(it)
        }
    }

    fun loadNewMovies() {
        loadMovies(Unit) {
            when (it) {
                is Success -> handleMovie(it.data)
                is Failure -> handleError(it.error)
            }
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            MOVIES -> if (favorite) removeMovieFromFavorites(id)
            else addMovieToFavorites(id)
            FAVORITE -> removeMovieFromFavorites(id)
        }
    }

    private fun handleMovie(list: List<Movie>) {
        movies.value = list
    }

    private fun handleFavoriteMovie(list: List<Movie>) {
        favoriteMovies.value = list
    }

    private fun addMovieToFavorites(id: String) {
        updateMovie(Params(id, true)) {
            handlePairResult(it)
        }
    }

    private fun removeMovieFromFavorites(id: String) {
        updateMovie(Params(id, false)) {
            handlePairResult(it)
        }
    }

    private fun handlePairResult(result: Result<Error, Pair<List<Movie>, List<Movie>>>) {
        when (result) {
            is Success -> {
                handleMovie(result.data.first)
                handleFavoriteMovie(result.data.second)
            }
            is Failure -> handleError(result.error)
        }
    }
}
