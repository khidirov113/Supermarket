package com.example.supermarket.domain.usecase.category

import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.repository.CatalogRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase @Inject constructor(
    private val repository: CatalogRepository
) {
    suspend operator fun invoke(): Flow<List<Category>> {
       return repository.getCategories()
    }
}