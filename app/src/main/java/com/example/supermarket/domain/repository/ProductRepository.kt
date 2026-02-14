package com.example.supermarket.domain.repository

import com.example.supermarket.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getWeekSales(): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    suspend fun getSimilarProducts(productId: Long): List<Product>

}