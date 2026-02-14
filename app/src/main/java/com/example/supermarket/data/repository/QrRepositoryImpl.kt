package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import android.util.Log
import com.example.supermarket.data.remote.network.QrApi
import com.example.supermarket.domain.entity.QrCodeData
import com.example.supermarket.domain.repository.QrRepository
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(
    private val api: QrApi
) : QrRepository {

    private val TAG = "QrRepoImpl"

    override suspend fun getAccessCode(): Result<QrCodeData> {
        Log.d(TAG, "getAccessCode: Request started")
        return try {
            val response = api.getAccessCode()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.toDomain()
                Log.d(TAG, "getAccessCode: SUCCESS. Code: ${data.code}")
                Result.success(data)
            } else {
                Log.e(TAG, "getAccessCode: FAILED. Code: ${response.code()}")
                Result.failure(Exception("Error get"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAccessCode: EXCEPTION. ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    override suspend fun verifyAccessCode(code: Int): Result<Unit> {
        return try {
            val response = api.verifyAccessCode(mapOf("code" to code))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error verify"))
        } catch (e: Exception) { Result.failure(e) }
    }
}