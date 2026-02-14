package com.example.supermarket.data.remote.network


import com.example.supermarket.data.remote.dto.AuthResponse
import com.example.supermarket.data.remote.dto.SendCodeRequest
import com.example.supermarket.data.remote.dto.VerifyCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApi {
    @POST("/api/send-code")
    suspend fun sendCode(@Body request: SendCodeRequest): Response<Unit>

    @POST("/api/verify-code")
    suspend fun verifyCode(@Body request: VerifyCodeRequest): Response<AuthResponse>
}