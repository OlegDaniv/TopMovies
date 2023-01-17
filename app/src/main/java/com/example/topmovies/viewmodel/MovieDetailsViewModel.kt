package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topmovies.domain.GetMovieDetailsUseCase
import com.example.topmovies.models.domain.MovieDetails
import com.example.topmovies.utils.Result.Failure
import com.example.topmovies.utils.Result.Success

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    fun resolveMovieDetails(movieId: String) {
        getMovieDetailsUseCase(movieId) { data ->
            when (data) {
                is Failure -> {
                    handleError(data.error)
                }
                is Success -> {
                    data.result.let { _movieDetails.value = it }
                }
            }
        }
    }
}
