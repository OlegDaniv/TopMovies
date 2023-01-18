package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.GetMoviesPairUseCase
import com.example.topmovies.domain.LoadMoviesUseCase
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase
import com.example.topmovies.domain.UpdateFavoriteMovieUseCase.Params
import com.example.topmovies.models.domain.Movie
import com.example.topmovies.unit.EnumScreen
import com.example.topmovies.unit.EnumScreen.FAVORITE
import com.example.topmovies.unit.EnumScreen.MOVIES
import com.example.topmovies.utils.Result.Failure
import com.example.topmovies.utils.Result.Success

class MovieViewModel constructor(
    private val getMovies: GetMoviesPairUseCase,
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
        getMovies(Unit) {
            when (it) {
                is Success -> {
                    handleMovie(it.result.first)
                    handleFavoriteMovie(it.result.second)
                }
                is Failure -> {
                    Failure(it.error)
                }
            }
        }
    }

    fun loadNewMovies() {
        loadMovies(Unit) {
            when (it) {
                is Success -> handleMovie(it.result)
                is Failure -> Failure(it.error)
            }
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            MOVIES ->
                if (favorite) removeMovieFromFavorites(id)
                else addMovieToFavorites(id)
            FAVORITE ->
                removeMovieFromFavorites(id)
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
            when (it) {
                is Success -> {
                    handleMovie(it.result.first)
                    handleFavoriteMovie(it.result.second)
                }
                is Failure -> handleError(it.error)
            }
        }
    }

    private fun removeMovieFromFavorites(id: String) {
        updateMovie(Params(id, false)) {
            when (it) {
                is Success -> {
                    handleMovie(it.result.first)
                    handleFavoriteMovie(it.result.second)
                }
                is Failure -> handleError(it.error)
            }
        }
    }
}
