package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendPhone(phone: String): Result<Unit>
    suspend fun verifyCode(phone: String, code: String): Result<AuthResult>

    suspend fun saveToken(token: String)

    val token: Flow<String?>
}