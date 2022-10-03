package com.example.topmovies.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.model.MovieDetails
import com.example.topmovies.repository.MovieRepository
import com.example.topmovies.unit.DEF_API_KEY
import com.example.topmovies.unit.SETTING_PREF_USER_API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel(
    private val repository: MovieRepository, private val sharedPref: SharedPreferences
) : ViewModel() {
    
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _detailsErrorMassage = MutableLiveData<String>()
    val detailsErrorMassage: LiveData<String> = _detailsErrorMassage
    
    fun resolveMovieDetails(movieId: String) {
        repository.getMovieDetails(getApiKey(), movieId).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.body()?.errorMessage.isNullOrBlank()) _movieDetails.value =
                    response.body()
                else _detailsErrorMassage.value = response.body()?.errorMessage
            }
            
            override fun onFailure(call: Call<MovieDetails>, throwable: Throwable) {
                _detailsErrorMassage.value = throwable.message
            }
        })
    }
    
    private fun getApiKey(): String {
        sharedPref.getString(SETTING_PREF_USER_API_KEY, DEF_API_KEY).also {
            return if (it.isNullOrEmpty()) DEF_API_KEY
            else it
        }
    }
}
