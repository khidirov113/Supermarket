package com.example.supermarket.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("image_path") val image: String?,
    @SerialName("products_count") val productsCount: Long,
    val subcategories: List<SubCategoryDto>? = emptyList()
)