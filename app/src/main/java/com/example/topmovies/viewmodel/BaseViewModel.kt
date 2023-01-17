package com.example.topmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topmovies.utils.Error

abstract class BaseViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<Error>()
    val errorMessage: LiveData<Error> = _errorMessage

    protected fun handleError(error: Error) {
        _errorMessage.value = error
    }
}
