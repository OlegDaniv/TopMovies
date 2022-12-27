package com.example.topmovies.domain

import android.os.Handler
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, R> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun run(params: Params): Data<R>

    operator fun invoke(
        params: Params,
        onSuccess: (Data<R>) -> Unit = {}
    ) {
        executor.execute {
            val result = run(params)
            handler.post { onSuccess(result) }
        }
    }

    class None

    data class Data<T>(val data: T, val error: String = "")
}
