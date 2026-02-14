package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.QrApi
import com.example.supermarket.domain.entity.QrCodeData
import com.example.supermarket.domain.repository.QrRepository
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(
    private val api: QrApi
) : QrRepository {


    override suspend fun getAccessCode(): Result<QrCodeData> {
        return try {
            val response = api.getAccessCode()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.toDomain()
                Result.success(data)
            } else {
                Result.failure(Exception("Error get"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyAccessCode(code: Int): Result<Unit> {
        return try {
            val response = api.verifyAccessCode(mapOf("code" to code))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error verify"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}