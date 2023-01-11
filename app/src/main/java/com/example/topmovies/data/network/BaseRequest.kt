package com.example.topmovies.data.network

import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.Result.Error
import com.example.topmovies.domain.utils.Result.Success
import okio.IOException
import retrofit2.Call

abstract class BaseRequest {

    fun <Type, Result> request(
        call: Call<Type>,
        transform: (Type) -> Result,
    ): com.example.topmovies.domain.utils.Result<Failure, Result> {
        return try {
            val response = call.execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Success(transform((body)))
            } else {
                Error(Failure.ServerError)
            }
        } catch (exception: IOException) {
            Error(Failure.ServerError)
        }
    }
}
