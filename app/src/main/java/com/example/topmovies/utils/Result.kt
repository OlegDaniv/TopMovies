package com.example.topmovies.utils

sealed class Result<out Error, out Data> {
    data class Failure<out Error>(val error: Error) : Result<Error, Nothing>()
    data class Success<out Data>(val result: Data) : Result<Nothing, Data>()

    fun process(
        onError: (Error) -> Any = {},
        onSuccess: (Data) -> Any
    ) =
        when (this) {
            is Failure -> onError(error)
            is Success -> onSuccess(result)
        }
}
