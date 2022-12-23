package com.example.topmovies.utils

import androidx.appcompat.app.AppCompatDelegate.*
import com.example.topmovies.exeption.Failure
import com.example.topmovies.exeption.Failure.*
import com.example.topmovies.utils.ResultOf.*
import retrofit2.Call

const val THEME_MODE_AUTO = "Auto"
const val THEME_MODE_LIGHT = "Light"
const val THEME_MODE_DARK = "Dark"

enum class EnumScreen {
    MOVIES, FAVORITE
}

fun checkNightMode(mode: String?) {
    setDefaultNightMode(
        when (mode) {
            THEME_MODE_AUTO -> MODE_NIGHT_FOLLOW_SYSTEM
            THEME_MODE_DARK -> MODE_NIGHT_YES
            THEME_MODE_LIGHT -> MODE_NIGHT_NO
            else -> MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}

fun <T, R> request(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): ResultOf<Failure, R> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> Success(transform((response.body() ?: default)))
            false -> Failed(ServerError)
        }
    } catch (exception: Throwable) {
        Failed(ServerError)
    }
}
