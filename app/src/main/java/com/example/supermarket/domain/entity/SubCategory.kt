package com.example.supermarket.domain.entity

data class SubCategory(
    val id: Long,
    val title: String,
    val categoryId: Long,
    val products: List<Product>? = emptyList()
)