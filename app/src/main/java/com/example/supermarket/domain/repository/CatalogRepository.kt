package com.example.supermarket.domain.repository


import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    suspend fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryDetail(subCategoryId: Long): Category

    suspend fun searchProducts(query: String): Result<List<Product>>

}