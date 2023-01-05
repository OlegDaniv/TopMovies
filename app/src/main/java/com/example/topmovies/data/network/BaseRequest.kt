package com.example.topmovies.data.network

import com.example.topmovies.domain.exeption.Failure
import com.example.topmovies.domain.utils.ResultOf
import retrofit2.Call

abstract class BaseRequest {

    fun <T, R> request(
        call: Call<T>,
        transform: (T) -> R,
        default: T
    ): ResultOf<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> ResultOf.Success(transform((response.body() ?: default)))
                false -> ResultOf.Failed(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            ResultOf.Failed(Failure.ServerError)
        }
    }
}
