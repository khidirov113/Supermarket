package com.example.supermarket.domain.usecase.auth

import com.example.supermarket.domain.value.AuthResult
import com.example.supermarket.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, code: String): AuthResult {
        val auth = repository.verifyCode(phone, code)
        auth.token?.let { repository.saveToken(it) }
        return auth
    }
}