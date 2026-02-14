package com.example.supermarket.domain.usecase.product

import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Long): Product? {
        return repository.getProductById(id)
    }
}