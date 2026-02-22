package com.example.supermarket.domain.usecase.auth

import com.example.supermarket.domain.value.AuthResult
import com.example.supermarket.domain.repository.AuthRepository
import jakarta.inject.Inject

class VerifyCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, code: String): Result<AuthResult> {
        val result = repository.verifyCode(phone, code)

        result.onSuccess { auth ->
            auth.token?.let { repository.saveToken(it) }
        }

        return result
    }
}