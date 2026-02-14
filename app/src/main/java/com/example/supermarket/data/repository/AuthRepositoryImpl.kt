package com.example.supermarket.data.repository

import android.util.Log
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.remote.dto.SendCodeRequest
import com.example.supermarket.data.remote.dto.VerifyCodeRequest
import com.example.supermarket.data.remote.network.AuthApi
import com.example.supermarket.domain.entity.AuthResult
import com.example.supermarket.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    private val TAG = "AuthRepositoryImpl"

    override val token: Flow<String?> = tokenManager.token.onEach {
        Log.d(TAG, "Current Token Flow update: ${if (it != null) "TOKEN_EXISTS" else "NULL"}")
    }

    override suspend fun sendPhone(phone: String): Result<Unit> {
        Log.d(TAG, "sendPhone: Request started for phone: $phone")
        return try {
            val cleanPhone = phone.replace("+", "")
            Log.d(TAG, "sendPhone: Cleaned phone for API: $cleanPhone")

            val response = authApi.sendCode(SendCodeRequest(cleanPhone))

            if (response.isSuccessful) {
                Log.d(TAG, "sendPhone: SUCCESS")
                Result.success(Unit)
            } else {
                val errorJson = response.errorBody()?.string()
                Log.e(TAG, "sendPhone: FAILED. Code: ${response.code()}, Details: $errorJson")
                Result.failure(Exception("Ошибка валидации: $errorJson"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "sendPhone: EXCEPTION. ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    override suspend fun verifyCode(phone: String, code: String): Result<AuthResult> {
        return try {
            val response = authApi.verifyCode(VerifyCodeRequest(phone, code))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val finalToken = body.token ?: body.accessToken

                Log.d("AuthRepositoryImpl", "Final Extracted Token: $finalToken")

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
        Log.d(TAG, "saveToken: Saving new token to DataStore...")
        tokenManager.saveToken(token)
        Log.d(TAG, "saveToken: Token saved successfully")
    }
}