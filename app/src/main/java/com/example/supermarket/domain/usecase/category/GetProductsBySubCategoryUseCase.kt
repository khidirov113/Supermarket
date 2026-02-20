package com.example.supermarket.domain.usecase.category


import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.repository.CatalogRepository
import javax.inject.Inject

class GetProductsBySubCategoryUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(subCategoryId: Long): Category {
        return repository.getProductsBySubCategory(subCategoryId)
    }
}