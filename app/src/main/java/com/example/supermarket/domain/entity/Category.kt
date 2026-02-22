package com.example.supermarket.domain.entity

data class Category(
    val id: Long,
    val title: String,
    val image: String?,
    val productsCount: Long?,
    val subcategories: List<SubCategory>? = emptyList()
)