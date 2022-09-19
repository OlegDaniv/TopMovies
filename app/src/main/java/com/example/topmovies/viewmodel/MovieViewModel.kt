package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.Movie
import com.example.topmovies.model.MovieDetails
import com.example.topmovies.model.MovieObject
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val TAG = MovieViewModel::class.simpleName

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val sharedPref: SharedPreferences
) : ViewModel() {
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: MutableLiveData<List<Movie>> = _movies
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies
    private val _errorMassage = MutableLiveData<String>()
    val errorMassage: MutableLiveData<String> = _errorMassage
    
    fun resolveMovieDetails(movieId: String) {
        repository.getMovieDetails(getApiKey(), movieId).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                _movieDetails.postValue(response.body())
            }
            
            override fun onFailure(call: Call<MovieDetails>, throwable: Throwable) {
                Log.e(TAG, "${throwable.message}")
            }
        })
    }
    
    fun resolveMovies(favoriteMoviesId: List<String>) {
        repository.getMovies(getApiKey()).enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                if (response.isSuccessful) {
                    if (response.body()?.items?.isEmpty() == true) {
                        _errorMassage.postValue(response.body()?.errorMessage)
                    } else {
                        response.body()?.items?.let { movies ->
                            _movies.postValue(movies)
                            favoriteMoviesId.forEach { id ->
                                val currentMovie = movies.find { it.id == id }
                                currentMovie?.isFavorite = true
                            }
                        }
                    }
                } else {
                    _errorMassage.postValue(
                        when (response.code()) {
                            in 300..399 -> "Redirection you too many times"
                            in 400..499 -> "Please restart your client"
                            in 500..599 -> "An internal server error has occurred"
                            else -> "The error is unknown"
                        }
                    )
                }
            }
    
            override fun onFailure(call: Call<MovieObject>, throwable: Throwable) {
                _errorMassage.postValue(throwable.message)
            }
        })
    }
    
    fun saveFavoriteMovie() {
        _favoriteMovies.value?.forEach { movie ->
            sharedPref.edit().putString(movie.id, "").apply()
        }
    }
    
    fun resolveFavoriteMovies() {
        val favoriteMovies = _movies.value?.filter { it.isFavorite } ?: emptyList()
        _favoriteMovies.value = favoriteMovies
    }
    
    fun removeMoviePreference() = sharedPref.edit().clear().apply()
    
    fun getFavoriteMoviesId() = sharedPref.all.keys.toList()
    
    private fun getApiKey(): String {
        sharedPref.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY).also {
            return if (it.isNullOrEmpty()) DEF_API_KEY
            else it
        }
    }
}
