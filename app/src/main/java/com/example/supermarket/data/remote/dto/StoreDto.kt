package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreDto(
    @SerialName("name") val name: String,
    @SerialName("lat") val lat: Double,
    @SerialName("lng") val lng: Double,
    @SerialName("address") val address: String? = null
)
