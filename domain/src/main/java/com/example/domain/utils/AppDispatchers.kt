package com.example.domain.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface AppDispatchers {
    val main: MainCoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
