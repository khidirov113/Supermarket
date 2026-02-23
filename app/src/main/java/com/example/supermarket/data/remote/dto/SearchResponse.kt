package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("data") val products: List<ProductDto>,
    @SerialName("total") val total: Int
)