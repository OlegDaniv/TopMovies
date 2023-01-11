package com.example.topmovies.domain.utils

sealed class Result<out Blunder, out Outcome> {
    data class Error<out Blunder>(val error: Blunder) : Result<Blunder, Nothing>()
    data class Success<out Outcome>(val result: Outcome) : Result<Nothing, Outcome>()

    fun asSuccess() = this as Success<Outcome>

    fun asErrors() = this as Error<Blunder>
}
