package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.domain.GetMovieDetailsUseCase
import com.example.topmovies.models.MovieDetails

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _detailsErrorMassage = MutableLiveData<String>()
    val detailsErrorMassage: LiveData<String> = _detailsErrorMassage

    fun resolveMovieDetails(movieId: String) {
        getMovieDetailsUseCase(movieId, {
            _movieDetails.value = it
        }, {
            _detailsErrorMassage.value = it
        })
    }
}
