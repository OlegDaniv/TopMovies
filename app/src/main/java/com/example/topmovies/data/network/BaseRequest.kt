package com.example.topmovies.data.network

import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import retrofit2.Call

abstract class BaseRequest {

    fun <Type, Result> request(
        call: Call<Type>,
        transform: (Type) -> Result,
        default: Type
    ): ResultOf<Failure, Result> {
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
