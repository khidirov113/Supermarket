package com.example.supermarket.domain.usecase.product

import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeekSalesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getWeekSales()
    }
}