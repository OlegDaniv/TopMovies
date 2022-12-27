package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.GetMoviesUseCase
import com.example.topmovies.domain.LoadMoviesUseCase
import com.example.topmovies.domain.UpdateMovieUseCase
import com.example.topmovies.domain.UpdateMovieUseCase.Params
import com.example.topmovies.domain.UseCase.None
import com.example.topmovies.models.Movie
import com.example.topmovies.unit.EnumScreen

class MovieViewModel constructor(
    private val getMovies: GetMoviesUseCase,
    private val loadMovies: LoadMoviesUseCase,
    private val updateMovie: UpdateMovieUseCase,
) : BaseViewModel() {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()

    fun getObservableList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            EnumScreen.MOVIES -> movies
            EnumScreen.FAVORITE -> favoriteMovies
        }
    }

    fun getMovies() {
        getMovies(None()) {
            if (it.error.isNotEmpty()) {
                handledErrors(it.error)
            } else {
                handledMovie(it.data.movies)
                handledFavoriteMovie(it.data.favorite)
            }
        }
    }

    fun loadNewMovies() {
        loadMovies(None()) {
            if (it.error.isNotEmpty()) {
                handledErrors(it.error)
            } else {
                handledMovie(it.data)
            }
        }
    }

    fun addFavoriteMovie(id: String, favorite: Boolean, screen: EnumScreen) {
        when (screen) {
            EnumScreen.MOVIES ->
                if (favorite) removeMovieFromFavorites(id)
                else addMovieToFavorites(id)
            EnumScreen.FAVORITE ->
                removeMovieFromFavorites(id)
        }
    }

    private fun handledMovie(list: List<Movie>) {
        movies.value = list
    }

    private fun handledFavoriteMovie(list: List<Movie>) {
        favoriteMovies.value = list
    }

    private fun addMovieToFavorites(id: String) {
        updateMovie(Params(id, true)) {
            if (it.error.isNotEmpty()) {
                handledErrors(it.error)
            } else {
                handledMovie(it.data.movies)
                handledFavoriteMovie(it.data.favorite)
            }
        }
    }

    private fun removeMovieFromFavorites(id: String) {
        updateMovie(Params(id, false)) {
            if (it.error.isNotEmpty()) {
                handledErrors(it.error)
            } else {
                handledMovie(it.data.movies)
                handledFavoriteMovie(it.data.favorite)
            }
        }
    }
}
