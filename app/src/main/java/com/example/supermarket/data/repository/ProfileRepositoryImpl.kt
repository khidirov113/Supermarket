package com.example.supermarket.data.repository

import android.util.Log
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.ProfileApi
import com.example.supermarket.domain.entity.UserProfile
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

    private val TAG = "ProfileRepoImpl"

    override suspend fun getUserProfile(): Result<UserProfile> {
        Log.d(TAG, "getUserProfile: Request started")
        return try {
            val response = api.getUser()
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.toDomain()
                Log.d(TAG, "getUserProfile: SUCCESS. User: ${user.firstName} ${user.lastName}")
                Result.success(user)
            } else {
                Log.e(TAG, "getUserProfile: FAILED. Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Интернет билан муаммо: ${e.message}")
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
            Log.d(TAG, "updateProfile: Start updating. Name: $name, Born: $bornIn, Gender: $gender")
            val methodPart = "PATCH".toRequestBody("text/plain".toMediaTypeOrNull())
            val namePart = name?.toRequestBody("text/plain".toMediaTypeOrNull())
            val surnamePart = surname?.toRequestBody("text/plain".toMediaTypeOrNull())
            val bornInPart = bornIn?.toRequestBody("text/plain".toMediaTypeOrNull())
            val genderPart = gender?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imagePath?.let { path ->
                Log.d(TAG, "updateProfile: Processing image file at $path")
                val file = File(path)
                if (file.exists()) {
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                } else {
                    Log.e(TAG, "updateProfile: Image file NOT FOUND at $path")
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
                Log.d(TAG, "updateProfile: SUCCESS. Profile updated successfully.")
                Log.d(TAG, "Update Success")
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e(TAG, "updateProfile: FAILED. Code: ${response.code()}, Details: $errorMsg ")
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateProfile: EXCEPTION. ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        Log.d(TAG, "logout: Request started")
        return try {
            val response = api.logout()
            if (response.isSuccessful) {
                Log.d(TAG, "logout: Server success. Clearing local token...")
                dataStoreManager.saveToken("")
                Log.d(TAG, "logout: SUCCESS. Token cleared.")
                Result.success(Unit)
            } else {
                Log.e(TAG, "logout: FAILED. Code: ${response.code()}")
                Result.failure(Exception("Logout Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "logout: EXCEPTION. ${e.localizedMessage}")
            Result.failure(e)
        }
    }
}