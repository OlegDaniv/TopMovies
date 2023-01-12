package com.example.topmovies.data.utils

import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Error.ServerError
import com.example.topmovies.domain.utils.Result
import com.example.topmovies.domain.utils.Result.Failure
import com.example.topmovies.domain.utils.Result.Success
import okio.IOException
import retrofit2.Call

fun <T, R> Call<T>.unwrap(transform: (T) -> R): Result<Error, R> {
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
