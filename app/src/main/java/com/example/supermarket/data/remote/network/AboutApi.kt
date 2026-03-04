package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.AboutDto
import retrofit2.http.GET

interface AboutApi {
    @GET("/api/about")
    suspend fun getAbout(): AboutDto
}

