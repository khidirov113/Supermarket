package com.example.supermarket.domain.repository


import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.entity.Product

interface CatalogRepository {
    suspend fun getCategories(): Result<List<Category>>

    suspend fun getProductsBySubCategory(subCategoryId: Long): Result<List<Product>>

    suspend fun searchProducts(query: String): Result<List<Product>>

}