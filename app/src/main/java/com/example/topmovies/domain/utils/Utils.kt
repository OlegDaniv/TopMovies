package com.example.topmovies.domain.utils

    fun <TFirst, TSecond, Result> assent(
        first: TFirst?,
        second: TSecond?,
        block: (TFirst, TSecond) -> Result?
    ): Result? {
        return if (first != null && second != null) block(first, second) else null
    }
