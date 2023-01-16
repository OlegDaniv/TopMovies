package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.GetMovieDetailsUseCase
import com.example.topmovies.models.MovieDetails

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    fun resolveMovieDetails(movieId: String) {
        getMovieDetailsUseCase(movieId) { data ->
            if (data.error.isNotEmpty()) {
                handledErrors(data.error)
            } else {
                data.value.let {
                    _movieDetails.value = it
                }
            }
        }
    }
}
