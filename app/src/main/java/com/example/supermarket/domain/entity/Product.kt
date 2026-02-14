package com.example.supermarket.domain.entity

data class Product(
    val id: Long,
    val title: String,
    val description: String?,
    val inStock: Boolean,
    val price: Double,
    val salePrice: Double,
    val salePercent: Double,
    val unit: String?,
    val image: String,
    val images: List<String>
)