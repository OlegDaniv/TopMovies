package com.example.topmovies.domain.utils

sealed class ResultOf<out Error, out Result> {
    data class Failed<out Error>(val error: Error) : ResultOf<Error, Nothing>()
    data class Success<out Result>(val result: Result) : ResultOf<Nothing, Result>()

    fun fold(
        onFailed: (Error) -> Any,
        onSuccess: (Result) -> Any
    ) =
        when (this) {
            is Failed -> onFailed(error)
            is Success -> onSuccess(result)
        }

    fun getSuccess(): Result? {
        return if (this is Success) {
            result
        } else {
            null
        }
    }

    fun getFailed(): Error? {
        return if (this is Failed) {
            error
        } else {
            null
        }
    }
}
