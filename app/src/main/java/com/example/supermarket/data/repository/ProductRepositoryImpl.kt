package com.example.supermarket.data.repository

import android.util.Log
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.ProductApi
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {
    override fun getWeekSales(): Flow<List<Product>> = flow {
        try {
            val response = productApi.getWeekSales()
            Log.d("CHECK_DATA", "1. Server Response: $response")
            if (response.isEmpty()) {
                Log.w("CHECK_DATA", "Server response is empty.")
            }
            val domainList = response.map { it.toDomain() }

            Log.d("CHECK_DATA", "2. Mapped Data: $domainList")

            emit(domainList)
        } catch (e: Exception) {
            Log.e("CHECK_DATA", "3. ERROR: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getProductById(id: Long): Product {
        return productApi.getProductById(id).toDomain()
    }

    override suspend fun getSimilarProducts(productId: Long): List<Product> {
        return try {
            val response = productApi.getSimilarProducts(productId)
            response.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("REPO_ERROR", "getSimilarProducts error: ${e.message}")
            emptyList()
        }
    }
}