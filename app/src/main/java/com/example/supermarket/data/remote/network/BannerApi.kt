package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.BannerDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerApi {
    @GET("api/banners")
    suspend fun getBanners(): List<BannerDto>

    @GET("api/banners/{id}")
    suspend fun getBannerById(
        @Path("id") id: Long
    ): BannerDto
}