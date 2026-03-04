package com.example.supermarket.data.remote.network

import com.example.supermarket.data.remote.dto.UserProfileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("/api/user")
    suspend fun getUser(): UserProfileDto

    @Multipart
    @POST("/api/user/update")
    suspend fun updateProfile(
        @Part("_method") method: RequestBody,
        @Part("name") name: RequestBody?,
        @Part("surname") surname: RequestBody?,
        @Part("born_in") bornIn: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("fcm_token") fcmToken: RequestBody?
    )

    @GET("/api/logout")
    suspend fun logout()
}