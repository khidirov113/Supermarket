package com.example.supermarket.data.repository

import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.remote.dto.SendCodeRequest
import com.example.supermarket.data.remote.dto.VerifyCodeRequest
import com.example.supermarket.data.remote.network.AuthApi
import com.example.supermarket.domain.entity.AuthResult
import com.example.supermarket.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {


    override val token: Flow<String?> = tokenManager.token

    override suspend fun sendPhone(phone: String): Result<Unit> {
        return try {
            val cleanPhone = phone.replace("+", "")

            val response = authApi.sendCode(SendCodeRequest(cleanPhone))

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorJson = response.errorBody()?.string()
                Result.failure(Exception("Ошибка валидации: $errorJson"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyCode(phone: String, code: String): Result<AuthResult> {
        return try {
            val response = authApi.verifyCode(VerifyCodeRequest(phone, code))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val finalToken = body.token ?: body.accessToken

                Result.success(
                    AuthResult(
                        token = finalToken,
                        message = body.message
                    )
                )
            } else {
                Result.failure(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }
}