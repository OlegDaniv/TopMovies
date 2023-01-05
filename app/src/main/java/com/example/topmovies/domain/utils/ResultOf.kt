package com.example.topmovies.domain.utils

sealed class ResultOf<out Error, out Result> {
    data class Failed<out Error>(val error: Error) : ResultOf<Error, Nothing>()
    data class Success<out R>(val result: R) : ResultOf<Nothing, R>()

    fun fold(
        onFailed: (Error) -> Any,
        onSuccess: (Result) -> Any = {}
    ): Any =
        when (this) {
            is Failed -> onFailed(error)
            is Success -> onSuccess(result)
        }
}
