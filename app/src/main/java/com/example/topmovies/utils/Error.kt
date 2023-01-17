package com.example.topmovies.utils

sealed class Error {
    object NetworkConnectionError : Error()
    object ServerError : Error()
}
