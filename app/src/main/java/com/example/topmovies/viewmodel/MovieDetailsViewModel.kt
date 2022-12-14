package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.models.MovieDetails
import com.example.topmovies.repository.MovieRepository

class MovieDetailsViewModel(
    private val repository: MovieRepository, sharedPref: SharedPreferences
) : BaseViewModel(sharedPref) {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _detailsErrorMassage = MutableLiveData<String>()
    val detailsErrorMassage: LiveData<String> = _detailsErrorMassage

    fun resolveMovieDetails(movieId: String) = with(repository) {
        loadMovieDetailsById(movieId) { movieDetailsEntity ->
            movieDetailsEntity?.let { _movieDetails.postValue(it.toMovieDetails()) }
                ?: loadMovieDetails(
                    apikey = getApiKey(),
                    movieId = movieId,
                    onSuccess = {
                        _movieDetails.postValue(it.toMovieDetails())
                    },
                    onError = { _detailsErrorMassage.postValue(it) })
        }
    }
}
