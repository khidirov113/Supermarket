package com.example.supermarket.domain.usecase.auth

import com.example.supermarket.domain.repository.AuthRepository
import javax.inject.Inject

class SendCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String) = repository.sendPhone(phone)
}