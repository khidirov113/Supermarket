package com.example.supermarket.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.supermarket.domain.entity.Settings
import com.example.supermarket.domain.repository.SettingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingRepository {
    private val notificationsEnabledKey = booleanPreferencesKey("notifications_enabled")
    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            val notificationEnabled =
                preferences[notificationsEnabledKey] ?: Settings.DEFAULT_NOTIFICATIONS_ENABLED

            Settings(
                notificationsEnabled = notificationEnabled,
            )
        }
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notificationsEnabledKey] = enabled
        }
    }
}