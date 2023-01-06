package com.example.topmovies.data.network

import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import com.example.topmovies.domain.utils.ResultOf.Failed
import com.example.topmovies.domain.utils.ResultOf.Success
import retrofit2.Call

abstract class BaseRequest {

    fun <Type, Result> request(
        call: Call<Type>,
        transform: (Type) -> Result,
        default: Type
    ): ResultOf<Failure, Result> {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                Success(transform((response.body() ?: default)))
            } else {
                Failed(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Failed(Failure.ServerError)
        }
    }
}
