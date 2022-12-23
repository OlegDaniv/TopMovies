package com.example.topmovies.exeption

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
}
