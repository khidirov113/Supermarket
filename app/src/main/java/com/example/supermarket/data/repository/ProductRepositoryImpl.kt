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
            val domainList = response.map { it.toDomain() }
            emit(domainList)
            Log.d("ProductRepositoryImpl", "getWeekSales: ${domainList.size}")
        } catch (e: Exception) {
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
            emptyList()
        }
    }
}