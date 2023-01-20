package com.example.topmovies.utils

import com.example.domain.utils.Error
import com.example.domain.utils.Error.ServerError
import com.example.domain.utils.Result
import com.example.domain.utils.Result.Failure
import com.example.domain.utils.Result.Success
import retrofit2.Call
import java.io.IOException

fun <T, R> Call<T>.safeTransform(transform: (T) -> R): Result<Error, R> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Success(transform((body)))
        } else {
            Failure(ServerError)
        }
    } catch (exception: IOException) {
        Failure(ServerError)
    }
}
