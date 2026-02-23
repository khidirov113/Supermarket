package com.example.supermarket.domain.usecase.user

import com.example.supermarket.domain.repository.ProfileRepository
import jakarta.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() {
        return repository.logout()
    }
}