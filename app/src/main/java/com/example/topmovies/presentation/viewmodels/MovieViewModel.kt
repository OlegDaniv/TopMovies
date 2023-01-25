package com.example.topmovies.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Movie
import com.example.domain.usecase.GetMoviesPairUseCase
import com.example.domain.usecase.LoadMoviesUseCase
import com.example.domain.usecase.Params
import com.example.domain.usecase.UpdateFavoriteMovieUseCase
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import com.example.topmovies.presentation.utils.AppDispatchers
import com.example.topmovies.presentation.utils.EnumScreen
import com.example.topmovies.presentation.utils.EnumScreen.FAVORITE
import com.example.topmovies.presentation.utils.EnumScreen.MOVIES
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel constructor(
    private val getMoviesPair: GetMoviesPairUseCase,
    private val loadMovies: LoadMoviesUseCase,
    private val updateMovie: UpdateFavoriteMovieUseCase,
    private val appDispatchers: AppDispatchers
) : BaseViewModel() {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()

    fun getObservableList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            MOVIES -> movies
            FAVORITE -> favoriteMovies
        }
    }

    fun getMovies() = viewModelScope.launch(appDispatchers.io) {
        val result = getMoviesPair(Unit)
        withContext(appDispatchers.main) {
            when (result) {
                is Success -> handlePairResult(result.data)
                is Failure -> handleError(result.error)
            }
        }
    }

    fun loadNewMovies() = viewModelScope.launch(appDispatchers.io) {
        val r = loadMovies(Unit)
        withContext(appDispatchers.main) {
            when (r) {
                is Success -> handleMovie(r.data)
                is Failure -> handleError(r.error)
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

    private fun addMovieToFavorites(id: String) = viewModelScope.launch {
        val result = updateMovie(Params(id, true))
        withContext(appDispatchers.main) {
            when (result) {
                is Success -> handlePairResult(result.data)
                is Failure -> handleError(result.error)
            }
        }
    }

    private fun removeMovieFromFavorites(id: String) = viewModelScope.launch {
        val result = updateMovie(Params(id, false))
        withContext(appDispatchers.main) {
            when (result) {
                is Success -> handlePairResult(result.data)
                is Failure -> handleError(result.error)
            }
        }
    }

    private fun handleMovie(list: List<Movie>) {
        movies.value = list
    }

    private fun handleFavoriteMovie(list: List<Movie>) {
        favoriteMovies.value = list
    }

    private fun handlePairResult(result: Pair<List<Movie>, List<Movie>>) {
        handleMovie(result.first)
        handleFavoriteMovie(result.second)
    }
}
