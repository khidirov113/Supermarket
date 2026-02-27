package com.example.supermarket.data.repository

import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.remote.dto.SendCodeRequest
import com.example.supermarket.data.remote.dto.VerifyCodeRequest
import com.example.supermarket.data.remote.network.AuthApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.value.AuthResult
import com.example.supermarket.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {


    override val token: Flow<String?> = tokenManager.token

    override suspend fun sendPhone(phone: String) {
        try {
            val cleanPhone = phone.replace("+", "")
            authApi.sendCode(SendCodeRequest(cleanPhone))

        } catch (e: IOException) {
            throw AppException.NetworkException()

        } catch (e: HttpException) {
            when (e.code()) {
                422 -> {
                    throw AppException.InvalidPhoneException()
                }

                400, 404 -> {
                    throw AppException.InvalidPhoneException()
                }

                429 -> {
                    throw AppException.TooManyRequestsException()
                }

                else -> {
                    throw AppException.ServerException(e.code())
                }
            }
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun verifyCode(phone: String, code: String): AuthResult {
        return try {
            val response = authApi.verifyCode(VerifyCodeRequest(phone, code))
            val finalToken = response.token ?: response.accessToken ?: ""

            AuthResult(
                token = finalToken,
                message = response.message
            )
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            if (e.code() == 422 || e.code() == 400) {
                throw AppException.InvalidCodeException()
            } else {
                throw AppException.ServerException(e.code())
            }
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }
}