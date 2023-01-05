package com.example.topmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.domain.exeption.Failure
import com.example.topmovies.presentation.utils.SingleEventLiveData

abstract class BaseViewModel : ViewModel() {

    private val _errorMessage = SingleEventLiveData<Failure>()
    val errorMessage: LiveData<Failure> = _errorMessage

    protected fun handledErrors(error: Failure) {
        _errorMessage.value = error
    }
}
