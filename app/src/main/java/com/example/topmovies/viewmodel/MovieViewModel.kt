package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.model.Movie
import com.example.topmovies.repository.MovieRepository

class MovieViewModel constructor(
    private val repository: MovieRepository,
    sharedPref: SharedPreferences,
    private val favoritePref: SharedPreferences
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
            onSuccess = { _movies.postValue(favoriteMovie(it.items)) },
            onError = { errorMessage.postValue(it) }
        )
    }
    
    fun saveFavoriteMovie(movieId: String) = favoritePref.edit().putString(movieId, "").apply()
    
    fun resolveFavoriteMovies() {
        _favoriteMovies.value = _movies.value?.filter { it.isFavorite } ?: emptyList()
    }
    
    fun removeFavoriteMovie(movie: Movie) {
        favoritePref.edit().remove(movie.id).apply()
        _favoriteMovies.postValue(
            _favoriteMovies.value?.toMutableList()?.apply { remove(movie) }?.toList()
        )
    }
    
    private fun favoriteMovie(items: List<Movie>): List<Movie> {
        getFavoriteMoviesId().forEach { id ->
            items.find { it.id == id }?.isFavorite = true
        }
        return items
    }
    
    private fun getFavoriteMoviesId(): List<String> {
        return favoritePref.all.keys.toList()
    }
}
