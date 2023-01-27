package com.example.topmovies.presentation.utils

import com.example.domain.utils.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class AppDispatchersImpl(
    override val main: MainCoroutineDispatcher = Dispatchers.Main,
    override val io: CoroutineDispatcher = Dispatchers.IO,
    override val default: CoroutineDispatcher = Dispatchers.Default,
) : AppDispatchers
