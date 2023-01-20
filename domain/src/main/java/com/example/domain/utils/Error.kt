package com.example.domain.utils

sealed class Error {
    object NetworkConnectionError : Error()
    object ServerError : Error()
}
