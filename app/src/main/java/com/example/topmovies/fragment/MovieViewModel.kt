package com.example.topmovies.fragment

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

val TAG = BaseFragment::class.simpleName

class MovieViewModel constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _favoriteMovies = MutableLiveData<MutableList<Movie>>()
    val favoriteMovies: LiveData<MutableList<Movie>> = _favoriteMovies

    fun getMovieDetails(movieId: String) {
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

    fun getMovies() {
        repository.getMovies().enqueue(object : Callback<MovieObject> {
            override fun onResponse(call: Call<MovieObject>, response: Response<MovieObject>) {
                _movies.postValue(response.body()?.items)
            }

            override fun onFailure(call: Call<MovieObject>, throwable: Throwable) {
                Log.e(TAG, "${throwable.message}")
            }
        })
    }

    fun getFavoriteMovies(moviesID: List<String>) {
        val favoriteMovies = mutableListOf<Movie>()
        moviesID.forEach { id ->
            val currentMovie = _movies.value?.find { it.id == id }
            if (currentMovie != null) favoriteMovies.add(currentMovie)
        }
        _favoriteMovies.value = favoriteMovies
    }

    fun updateFavoriteList(id: String) {
        val movie = _favoriteMovies.value?.find { it.id == id }
        _favoriteMovies.value?.toMutableList()?.apply { remove(movie) }?.toList()
    }
}
