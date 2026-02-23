package com.example.supermarket.domain.usecase.user


import com.example.supermarket.domain.repository.ProfileRepository
import com.example.supermarket.domain.value.UserProfile
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): UserProfile {
        return repository.getUserProfile()
    }
}