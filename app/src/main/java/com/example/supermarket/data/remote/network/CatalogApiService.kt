package com.example.supermarket.data.remote.network


import com.example.supermarket.data.remote.dto.CategoryDto
import com.example.supermarket.data.remote.dto.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApiService {
    @GET("/api/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("/api/categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Long
    ): CategoryDto

    @GET("/api/products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("page") page: Int
    ): SearchResponse

}