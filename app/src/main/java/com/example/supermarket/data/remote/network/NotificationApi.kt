package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.NotificationResponse
import retrofit2.http.GET


interface NotificationApi {
    @GET("/api/notifications")
    suspend fun getNotifications(): NotificationResponse

    @GET("/api/news")
    suspend fun getNews(): NotificationResponse
}