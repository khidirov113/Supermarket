package com.example.supermarket.domain.repository


import androidx.paging.PagingData
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    suspend fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryDetail(subCategoryId: Long): Category

    fun searchProducts(query: String, onTotalCount: (Int) -> Unit): Flow<PagingData<Product>>
}