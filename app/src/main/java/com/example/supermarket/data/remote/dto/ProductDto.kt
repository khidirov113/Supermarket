package com.example.supermarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProductDto(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    @SerialName("in_stock")
    val inStock: Int? = 0,
    @SerialName("price")
    val price: Double? = 0.0,
    val unit: String? = null,
    @SerialName("sale_price")
    val finalPrice: Double? = 0.0,
    @SerialName("sale_percent")
    val salePercent: Double? = 0.0,
    @SerialName("image_path")
    val image: String? = null,
    @SerialName("images")
    val images: List<ImageRemoteDto>? = null,
)

@Serializable
data class ImageRemoteDto(
    @SerialName("image_path") val imagePath: String?
)