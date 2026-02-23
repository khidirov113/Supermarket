package com.example.supermarket.domain.usecase.category

import com.example.supermarket.domain.entity.SearchProductsResult
import com.example.supermarket.domain.repository.CatalogRepository
import jakarta.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(query: String): SearchProductsResult{
        return repository.searchProducts(query)
    }
}