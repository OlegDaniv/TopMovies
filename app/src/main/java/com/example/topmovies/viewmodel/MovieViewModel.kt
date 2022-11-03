package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.model.Movie
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.ALL_MOVIES_SCREEN
import com.example.topmovies.unit.FAVOURITE_MOVIES_SCREEN
import com.example.topmovies.unit.Screen

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val favoritePref: SharedPreferences,
    sharedPref: SharedPreferences
) : BaseViewModel(sharedPref) {
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    private val _favoriteMovies = mutableListOf<Movie>()
    val favoriteMovies: LiveData<List<Movie>> = MutableLiveData(_favoriteMovies)
    private var _errorMessage: String? = null
    val errorMessage = MutableLiveData(_errorMessage)
    
    private fun <T> LiveData<T>.update() {
        (this as? MutableLiveData<T>)?.let {
            value = value
        }
    }
    
    fun resolveMovies() {
        _favoriteMovies.clear()
        repository.getNewMovies(
            getApiKey(),
            onSuccess = {
                adjustFavoriteMovies(it.items)
                _movies.postValue(it.items)
            },
            onError = { errorMessage.postValue(it) }
        )
    }
    
    private fun adjustFavoriteMovies(movies: List<Movie>) {
        favoritePref.all.keys.toList().forEach { id ->
            movies.find { it.id == id }?.let { chosenMovie ->
                chosenMovie.isFavorite = true
                _favoriteMovies.add(chosenMovie)
            }
        }
    }
    
    fun addFavoriteMovie(movie: Movie, @Screen screen: Int) {
        when (screen) {
            ALL_MOVIES_SCREEN -> {
                if (movie.isFavorite) {
                    removeMovieFromFavorites(movie)
                } else {
                    addMovieToFavorites(movie)
                }
            }
            FAVOURITE_MOVIES_SCREEN -> removeMovieFromFavorites(movie)
        }
        favoriteMovies.update()
    }
    
    private fun addMovieToFavorites(movie: Movie) {
        val mutableMovies = _movies.value!!.toMutableList()
        val copy = movie.copy(isFavorite = true)
        mutableMovies[_movies.value!!.indexOf(movie)] = copy
        _movies.postValue(mutableMovies)
        _favoriteMovies.add(copy)
        favoritePref.edit().putString(movie.id, "").apply()
    }
    
    private fun removeMovieFromFavorites(movie: Movie) {
        val mutableMovies = _movies.value!!.toMutableList()
        val copy = movie.copy(isFavorite = false)
        mutableMovies[_movies.value!!.indexOf(movie)] = copy
        _movies.postValue(mutableMovies)
        _favoriteMovies.remove(movie)
        favoritePref.edit().remove(movie.id).apply()
    }
}

