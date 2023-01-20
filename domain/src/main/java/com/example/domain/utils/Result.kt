package com.example.domain.utils

sealed class Result<out Error, out Data> {
    data class Failure<out Error>(val error: Error) : Result<Error, Nothing>()
    data class Success<out Data>(val result: Data) : Result<Nothing, Data>()

    fun asSuccess() = this as? Success<Data>

    fun process(
        onError: (Error) -> Any = {},
        onSuccess: (Data) -> Any
    ) = when (this) {
        is Failure -> onError(error)
        is Success -> onSuccess(result)
    }
}

fun <TFirst, TSecond, Result> safeLet(
    first: TFirst?,
    second: TSecond?,
    block: (TFirst, TSecond) -> Result?
): Result? {
    return if (first != null && second != null) block(first, second) else null
}
