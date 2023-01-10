package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.domain.utils.Failure
import com.example.topmovies.domain.utils.ResultOf
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, Result> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun execute(params: Params): ResultOf<Failure, Result>

    operator fun invoke(
        params: Params,
        onSuccess: (ResultOf<Failure, Result>) -> Unit = {}
    ) {
        executor.execute {
            val result = execute(params)
            handler.post { onSuccess(result) }
        }
    }
}
