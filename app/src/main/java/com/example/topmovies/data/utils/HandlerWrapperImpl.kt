package com.example.topmovies.data.utils

import android.os.Handler
import com.example.domain.utils.HandlerWrapper

class HandlerWrapperImpl(
    private val handler: Handler
) : HandlerWrapper {

    override fun post(runnable: Runnable) {
        handler.post(runnable)
    }
}
