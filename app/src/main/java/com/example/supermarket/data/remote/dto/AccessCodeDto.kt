package com.example.supermarket.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.supermarket.domain.entity.QrCodeData

@Serializable
data class AccessCodeDto(
    @SerialName("code") val code: String? = null,

    @SerialName("try_after") val tryAfter: Int? = null,

    @SerialName("expires_at") val expiresAt: String? = null
)


