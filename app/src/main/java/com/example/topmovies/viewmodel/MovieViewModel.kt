package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.Movie
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val sharedPref: SharedPreferences,
    private val favoritePref: SharedPreferences
) : ViewModel() {
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies
    private var _errorMessage: String? = null
    val errorMessage = MutableLiveData(_errorMessage)
    
    fun resolveMovies() {
        repository.getNewMovies(
            getApiKey(),
            onSuccess = {
                _movies.value = it.items
                favoriteMovie()
            },
            onError = { errorMessage.value = it }
        )
    }
    
    fun saveFavoriteMovie() = _favoriteMovies.value?.forEach { movie ->
        favoritePref.edit().putString(movie.id, "").apply()
    }
    
    fun removeMoviePreference() {
        favoritePref.edit().clear().apply()
    }
    
    fun resolveFavoriteMovies() {
        _favoriteMovies.value = _movies.value?.filter { it.isFavorite }
    }
    
    private fun favoriteMovie() =
        getFavoriteMoviesId().forEach { id ->
            _movies.value?.find { it.id == id }?.isFavorite = true
        }
    
    private fun getFavoriteMoviesId() = favoritePref.all.keys.toList()
    
    private fun getApiKey() =
        sharedPref.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY)?.takeIf { it.isNotBlank() }
            ?: DEF_API_KEY
}

