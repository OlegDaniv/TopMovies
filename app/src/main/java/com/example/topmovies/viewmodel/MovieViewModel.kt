package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.model.Movie
import com.example.topmovies.repository.MovieRepository

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val favoritePref: SharedPreferences,
    sharedPref: SharedPreferences
) : BaseViewModel(sharedPref) {
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies
    private var _errorMessage: String? = null
    val errorMessage = MutableLiveData(_errorMessage)
    
    fun resolveMovies() {
        repository.getNewMovies(
            getApiKey(),
            onSuccess = { _movies.postValue(it.items) },
            onError = { errorMessage.postValue(it) }
        )
    }
    
    fun saveFavoriteMovie(movieId: String) = favoritePref.edit().putString(movieId, "").apply()
    
    fun removeFavoriteMovie(movie: Movie) {
        favoritePref.edit().remove(movie.id).apply()
        _favoriteMovies.postValue(
            _favoriteMovies.value?.toMutableList()?.apply { remove(movie) }?.toList()
        )
    }
    
}
