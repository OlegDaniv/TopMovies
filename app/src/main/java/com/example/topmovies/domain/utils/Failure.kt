package com.example.topmovies.domain.utils

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
}
