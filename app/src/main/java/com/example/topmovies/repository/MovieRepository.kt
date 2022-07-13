package com.example.topmovies.repository

import com.example.topmovies.retrofit.ApiServer

class MovieRepository constructor(private val apiServer: ApiServer) {
    fun getAllMovies() = apiServer.getAllMovies()
}
