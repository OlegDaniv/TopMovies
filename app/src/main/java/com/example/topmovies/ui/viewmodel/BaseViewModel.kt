package com.example.topmovies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    private val _errorMessage = SingleLiveEvent<Failure>()
    val errorMessage: LiveData<Failure> = _errorMessage

    protected fun handledErrors(error: Failure) {
        _errorMessage.value = error
    }
}
