package com.example.supermarket.domain.usecase.category

import androidx.paging.PagingData
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.CatalogRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchProductsUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    operator fun invoke(query: String, onTotalCount: (Int) -> Unit): Flow<PagingData<Product>> {
        return repository.searchProducts(query, onTotalCount)
    }
}