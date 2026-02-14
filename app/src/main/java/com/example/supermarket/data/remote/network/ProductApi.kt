package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("api/week-sales")
    suspend fun getWeekSales(): List<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Long
    ): ProductDto
    @GET("api/products/similar/{id}")
    suspend fun getSimilarProducts(
        @Path("id") id: Long
    ): List<ProductDto>

}