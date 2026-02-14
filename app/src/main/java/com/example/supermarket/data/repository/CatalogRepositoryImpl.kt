package com.example.supermarket.data.repository


import android.util.Log
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.CatalogApiService
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: CatalogApiService
) : CatalogRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductsBySubCategory(subCategoryId: Long): Result<List<Product>> {
        return try {
            val response = apiService.getProductsBySubCategory(subCategoryId)
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = apiService.searchProducts(query)
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("CatalogRepositoryImpl", "Search products: $responseBody")
                val productsDto = responseBody?.products
                Log.d("CatalogRepositoryImpl", "Search products: $productsDto")
                val products = productsDto?.map { it.toDomain() } ?: emptyList()
                Log.d("CatalogRepositoryImpl", "Search products: ${products.size}")
                Result.success(products)
            } else {
                Result.failure(Exception("Error : ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

