package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.Notification

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notification>>
    suspend fun getNews(): Result<List<Notification>>
}