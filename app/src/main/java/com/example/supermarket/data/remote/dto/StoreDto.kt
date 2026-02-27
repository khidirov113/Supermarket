package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class StoreDto(
    val id: Int,
    val title: String,
    val address: String? = null,
    val longitude: String,
    val latitude: String,
    @SerialName("from_time") val fromTime: String? = null,
    @SerialName("to_time") val toTime: String? = null,
    val images: List<String>? = emptyList()
)
