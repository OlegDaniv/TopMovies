package com.example.topmovies.repository

import com.example.topmovies.retrofit.ApiServer

class MovieRepository constructor(private val apiServer: ApiServer = ApiServer.getInstance()) {

    fun getAllMovies() = apiServer.getAllMovies()
}
