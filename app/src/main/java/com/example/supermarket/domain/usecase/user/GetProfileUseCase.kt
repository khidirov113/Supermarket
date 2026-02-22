package com.example.supermarket.domain.usecase.user


import com.example.supermarket.domain.value.UserProfile
import com.example.supermarket.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getUserProfile()
    }
}