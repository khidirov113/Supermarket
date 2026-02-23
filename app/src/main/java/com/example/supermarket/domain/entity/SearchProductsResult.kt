package com.example.supermarket.domain.entity


data class SearchProductsResult(
    val products: List<Product>,
    val total: Int
)