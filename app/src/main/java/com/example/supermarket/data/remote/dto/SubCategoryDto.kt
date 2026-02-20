package com.example.supermarket.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SubCategoryDto(
    val id: Long? = null,
    val title: String? = null,
    @SerialName("category_id") val categoryId: Long? = null,
    val products: List<ProductDto>? = emptyList()
)