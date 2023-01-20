package com.example.topmovies.models.mapper

interface Mapper<T : Any, Model : Any> {
    fun toModel(value: T): Model
    fun fromModel(value: Model): T
}