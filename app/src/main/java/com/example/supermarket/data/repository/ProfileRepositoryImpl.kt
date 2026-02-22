package com.example.supermarket.data.repository

import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.ProfileApi
import com.example.supermarket.domain.value.UserProfile
import com.example.supermarket.domain.repository.ProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val dataStoreManager: TokenManager
) : ProfileRepository {


    override suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val response = api.getUser()
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.toDomain()
                Result.success(user)
            } else {
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)

        }
    }

    override suspend fun updateProfile(
        name: String?,
        surname: String?,
        bornIn: String?,
        gender: Int?,
        imagePath: String?
    ): Result<Unit> {
        return try {
            val methodPart = "PATCH".toRequestBody("text/plain".toMediaTypeOrNull())
            val namePart = name?.toRequestBody("text/plain".toMediaTypeOrNull())
            val surnamePart = surname?.toRequestBody("text/plain".toMediaTypeOrNull())
            val bornInPart = bornIn?.toRequestBody("text/plain".toMediaTypeOrNull())
            val genderPart = gender?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imagePath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                } else {
                    null
                }
            }
            val response = api.updateProfile(
                methodPart,
                namePart,
                surnamePart,
                bornInPart,
                genderPart,
                imagePart
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = api.logout()
            if (response.isSuccessful) {
                dataStoreManager.saveToken("")
                Result.success(Unit)
            } else {
                Result.failure(Exception("Logout Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}