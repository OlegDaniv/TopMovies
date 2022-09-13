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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val TAG = MovieViewModel::class.simpleName

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val sharedPref: SharedPreferences
) : ViewModel() {
    
    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: MutableLiveData<List<Movie>?> = _movies
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies
    
    fun resolveMovieDetails(movieId: String) {
        repository.getMovieDetails(movieId).enqueue(object :
            Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                _movieDetails.postValue(response.body())
            }
            
            override fun onFailure(call: Call<MovieDetails>, throwable: Throwable) {
                Log.e(TAG, "${throwable.message}")
            }
        })
    }
    
    fun resolveMovies(favoriteMoviesId: List<String>) {
        repository.getMovies().enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                val topMovies = response.body()?.items
                favoriteMoviesId.forEach { id ->
                    val currentMovie = topMovies?.find { it.id == id }
                    currentMovie?.isFavorite = true
                }
                _movies.postValue(topMovies)
            }
    
            override fun onFailure(call: Call<MovieObject>, throwable: Throwable) {
                Log.e(TAG, "${throwable.message}")
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
}
