package com.example.supermarket.domain.usecase.user


import com.example.supermarket.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(token: String) {
        repository.updateFcmToken(token)
    }
}