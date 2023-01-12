package com.example.topmovies.domain.utils

sealed class Error {
    object NetworkConnection : Error()
    object ServerError : Error()
}
