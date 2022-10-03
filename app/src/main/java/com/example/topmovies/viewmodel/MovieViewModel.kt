package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.R
import com.example.topmovies.model.Movie
import com.example.topmovies.model.MovieObject
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY
import com.example.topmovies.unit.StringResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel constructor(
    private val repository: MovieRepository,
    private val sharedPref: SharedPreferences,
    private val res: StringResource
) : ViewModel() {
    
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: MutableLiveData<List<Movie>> = _movies
    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies
    private val _errorMessage = null
    val errorMessage = MutableLiveData<String?>(_errorMessage)
    
    fun resolveMovies(favoriteMoviesId: List<String>) {
        repository.getMovies(getApiKey()).enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                if (response.isSuccessful) {
                    if (response.body()?.errorMessage?.isNotEmpty() == true) {
                        errorMessage.value = response.body()?.errorMessage
                    } else {
                        response.body()?.items?.let { movies ->
                            _movies.postValue(movies)
                            favoriteMoviesId.forEach { id ->
                                val currentMovie = movies.find { it.id == id }
                                currentMovie?.isFavorite = true
                            }
                        }
                    }
                } else errorMessage.value = httpErrors(response.code())
            }
    
            override fun onFailure(call: Call<MovieObject>, throwable: Throwable) {
                errorMessage.value = throwable.message
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
            return if (it.isNullOrEmpty()) DEF_API_KEY else it
        }
    }
    
    private fun httpErrors(code: Int): String {
        return when (code) {
            in 300..399 -> res.getString(R.string.error_redirection)
            in 400..499 -> res.getString(R.string.error_client)
            in 500..599 -> res.getString(R.string.error_server)
            else -> res.getString(R.string.error_unknown)
        }
    }
}

