package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
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
}
