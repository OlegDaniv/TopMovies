package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.domain.utils.Failure
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, Result> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun execute(params: Params): com.example.topmovies.domain.utils.Result<Failure, Result>

    operator fun invoke(
        params: Params,
        onSuccess: (com.example.topmovies.domain.utils.Result<Failure, Result>) -> Unit = {}
    ) {
        executor.execute {
            val result = execute(params)
            handler.post { onSuccess(result) }
        }
    }

    fun <Type1, Type2, Result> let(
        first: Type1?,
        second: Type2?,
        block: (Type1, Type2) -> Result?
    ): Result? {
        return if (first != null && second != null) block(first, second) else null
    }
}
