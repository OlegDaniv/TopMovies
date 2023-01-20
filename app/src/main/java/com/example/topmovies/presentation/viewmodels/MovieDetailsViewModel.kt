package com.example.topmovies.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.models.MovieDetails
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success

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
                    data.data.let { _movieDetails.value = it }
                }
            }
        }
    }
}