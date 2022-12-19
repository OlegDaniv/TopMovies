package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.domain.*
import com.example.topmovies.models.Movie
import com.example.topmovies.unit.EnumScreen

class MovieViewModel constructor(
    private val getMovieUseCase: GetMoviesUseCase,
    private val upsertMoviesUseCase: UpsertMoviesUseCase,
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val favoriteMovieUseCase: GetFavoriteMovieUseCase,
) : ViewModel() {

    private val movies = MutableLiveData<List<Movie>>()
    private val favoriteMovies = MutableLiveData<List<Movie>>()
    private var _errorMessage: String? = null
    val errorMessage = MutableLiveData(_errorMessage)

    fun getMoviesList(screen: EnumScreen): LiveData<List<Movie>> {
        return when (screen) {
            EnumScreen.MOVIES -> movies
            EnumScreen.FAVORITE -> favoriteMovies
        }
    }

    fun loadMovies() {
        getMovieUseCase({ movies.value = it }, { errorMessage.value = it })
        favoriteMovieUseCase { favoriteMovies.value = it }
    }

    fun resolveMovies() {
        loadMoviesUseCase({ movie ->
            upsertMoviesUseCase(movie,
                { movies.value = it },
                { errorMessage.value = it })
        }, {
            errorMessage.value = it
        })
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

    private fun addMovieToFavorites(id: String) {
        updateMovieUseCase(id, true) { newMovies ->
            movies.value = newMovies
            favoriteMovies.value = movies.value?.filter { it.isFavorite }
        }

    }

    private fun removeMovieFromFavorites(id: String) {
        updateMovieUseCase(id, false) { nesMovies ->
            movies.value = nesMovies
            favoriteMovies.value = movies.value?.filter { it.isFavorite }
        }
    }
}
