package com.example.domain.usecase

import com.example.domain.utils.Error
import com.example.domain.utils.Result

abstract class UseCase<Params, Data> {

    abstract suspend fun execute(params: Params): Result<Error, Data>

    suspend operator fun invoke(params: Params): Result<Error, Data> = execute(params)
}
