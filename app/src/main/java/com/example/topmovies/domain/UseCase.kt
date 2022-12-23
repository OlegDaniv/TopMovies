package com.example.topmovies.domain

import android.os.Handler
import com.example.topmovies.exeption.Failure
import com.example.topmovies.utils.ResultOf
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, R> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun run(params: Params): ResultOf<Failure, R>

    operator fun invoke(
        params: Params,
        onSuccess: (ResultOf<Failure, R>) -> Unit = {}
    ) {
        executor.execute {
            val result = run(params)
            handler.post { onSuccess(result) }
        }
    }

    class None
}
