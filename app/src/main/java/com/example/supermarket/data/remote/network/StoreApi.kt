package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.StoreDto
import retrofit2.Response
import retrofit2.http.GET

interface StoreApi {
    @GET("api/stores")
    suspend fun getStores(): List<StoreDto>
}