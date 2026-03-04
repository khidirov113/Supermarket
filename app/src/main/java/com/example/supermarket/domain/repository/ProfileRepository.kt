package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.UserProfile

interface ProfileRepository {
    suspend fun updateProfile(
        name: String?,
        surname: String?,
        bornIn: String?,
        gender: Int?,
        imagePath: String?
    )

    suspend fun logout()

    suspend fun getUserProfile(): UserProfile

    suspend fun updateFcmToken(fcmToken: String)
}