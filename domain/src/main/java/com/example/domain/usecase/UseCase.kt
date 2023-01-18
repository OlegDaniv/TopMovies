package com.example.domain.usecase

import com.example.domain.utils.Error
import com.example.domain.utils.HandlerWrapper
import com.example.domain.utils.Result
import java.util.concurrent.ExecutorService

abstract class UseCase<Params, Data> {

    abstract val executor: ExecutorService
    abstract val handler: HandlerWrapper

    abstract fun execute(params: Params): Result<Error, Data>

    operator fun invoke(
        params: Params,
        onExecute: (Result<Error, Data>) -> Unit
    ) {
        executor.execute {
            val result = execute(params)
            handler.post { onExecute(result) }
        }
    }
}
