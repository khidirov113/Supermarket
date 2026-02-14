package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SendCodeRequest(val phone: String)

@Serializable
data class VerifyCodeRequest(
    val phone: String,
    val code: String
)

@Serializable
data class AuthResponse(
    @SerialName("token")
    val token: String? = null,

    @SerialName("access_token")
    val accessToken: String? = null,

    @SerialName("message")
    val message: String? = null
)