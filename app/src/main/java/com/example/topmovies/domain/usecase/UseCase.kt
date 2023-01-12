package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.domain.utils.Error
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, Result> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun execute(params: Params): com.example.topmovies.domain.utils.Result<Error, Result>

    operator fun invoke(
        params: Params,
        onSuccess: (com.example.topmovies.domain.utils.Result<Error, Result>) -> Unit = {}
    ) {
        executor.execute {
            val result = execute(params)
            handler.post { onSuccess(result) }
        }
    }

    fun <TFirst, TSecond, Result> let(
        first: TFirst?,
        second: TSecond?,
        block: (TFirst, TSecond) -> Result?
    ): Result? {
        return if (first != null && second != null) block(first, second) else null
    }
}
