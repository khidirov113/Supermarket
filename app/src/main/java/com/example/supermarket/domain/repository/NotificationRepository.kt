package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.Notification

interface NotificationRepository {
    suspend fun getNotifications(): List<Notification>
    suspend fun getNews(): List<Notification>
}