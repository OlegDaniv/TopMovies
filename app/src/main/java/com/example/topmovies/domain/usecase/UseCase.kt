package com.example.topmovies.domain.usecase

import android.os.Handler
import com.example.topmovies.domain.utils.Error
import com.example.topmovies.domain.utils.Result
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, Data> {

    abstract val executor: ExecutorService
    abstract val handler: Handler

    abstract fun execute(params: Params): Result<Error, Data>

    operator fun invoke(
        params: Params,
        onSuccess: (Result<Error, Data>) -> Unit = {}
    ) {
        executor.execute {
            val result = execute(params)
            handler.post { onSuccess(result) }
        }
    }


}
