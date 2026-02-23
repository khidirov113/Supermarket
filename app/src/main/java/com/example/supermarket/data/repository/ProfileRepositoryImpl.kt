package com.example.supermarket.data.repository

import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.ProfileApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.value.UserProfile
import com.example.supermarket.domain.repository.ProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val dataStoreManager: TokenManager
) : ProfileRepository {


    override suspend fun getUserProfile(): UserProfile {
        return try {
            val response = api.getUser()
            response.toDomain()
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun updateProfile(
        name: String?,
        surname: String?,
        bornIn: String?,
        gender: Int?,
        imagePath: String?
    ) {
        try {
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
                } else null
            }

            api.updateProfile(methodPart, namePart, surnamePart, bornInPart, genderPart, imagePart)

        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun logout() {
        try {
            api.logout()
            dataStoreManager.saveToken("")
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }
}