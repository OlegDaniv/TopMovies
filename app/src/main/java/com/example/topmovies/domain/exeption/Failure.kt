package com.example.topmovies.domain.exeption

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
}
