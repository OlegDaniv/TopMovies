package com.example.topmovies.unit

import android.content.Context

interface StringRes {
    
    fun getString(resId: Int): CharSequence
}

class StringResource(private val context: Context) : StringRes {
    
    override fun getString(resId: Int): String = context.getString(resId)
}
