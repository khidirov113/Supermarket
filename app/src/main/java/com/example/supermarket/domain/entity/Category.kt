package com.example.supermarket.domain.entity

data class Category(
    val id: Long,
    val name: String,
    val image: String?,
    val productsCount: Int
)