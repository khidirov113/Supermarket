package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.AccessCodeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QrApi {
    @POST("/api/access-code")
    suspend fun getAccessCode(): Response<AccessCodeDto>

    @POST("/api/access-code-verify")
    suspend fun verifyAccessCode(
        @Body request: Map<String, Int>
    ): Response<Unit>
}