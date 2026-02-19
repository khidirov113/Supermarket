package com.example.supermarket.domain.repository

import com.example.supermarket.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun getSettings(): Flow<Settings>

    suspend fun updateNotificationEnabled(enabled: Boolean)
}