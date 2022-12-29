package com.example.topmovies.domain

import android.os.Handler
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, R> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun run(params: Params): Result<R>

    operator fun invoke(
        params: Params,
        onSuccess: (Result<R>) -> Unit = {}
    ) {
        executor.execute {
            val result = run(params)
            handler.post { onSuccess(result) }
        }
    }

    class None

    data class Result<T>(val value: T, val error: String = "")
}
