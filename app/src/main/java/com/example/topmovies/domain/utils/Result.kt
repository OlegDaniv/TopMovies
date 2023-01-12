package com.example.topmovies.domain.utils

sealed class Result<out Error, out Data> {
    data class Failure<out Error>(val error: Error) : Result<Error, Nothing>()
    data class Success<out Data>(val result: Data) : Result<Nothing, Data>()

    fun asSuccess() = this as Success<Data>

    fun asErrors() = this as Failure<Error>
}
