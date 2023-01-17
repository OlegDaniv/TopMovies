package com.example.topmovies.utils

sealed class Error {
    object NetworkConnection : Error()
    object ServerError : Error()
}
