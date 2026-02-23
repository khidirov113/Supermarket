package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.ProductApi
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {
    override fun getWeekSales(): Flow<List<Product>> = flow {
        try {
            val response = productApi.getWeekSales()
            emit(response.map { it.toDomain() })
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
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