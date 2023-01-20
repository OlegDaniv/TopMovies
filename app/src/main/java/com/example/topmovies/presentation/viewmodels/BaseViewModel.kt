package com.example.topmovies.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.domain.utils.Error
import com.example.topmovies.presentation.utils.SingleEventLiveData

abstract class BaseViewModel : ViewModel() {

    private val _errorMessage = SingleEventLiveData<Error>()
    val errorMessage: LiveData<Error> = _errorMessage

    protected fun handleError(error: Error) {
        _errorMessage.value = error
    }
}
