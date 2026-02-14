package com.example.supermarket.domain.usecase.category

import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.CatalogRepository
import jakarta.inject.Inject

class GetProductsBySubCategoryUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(subCategoryId: Long): Result<List<Product>> =
        repository.getProductsBySubCategory(subCategoryId)
}