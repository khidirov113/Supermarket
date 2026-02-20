package com.example.supermarket.data.repository


import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.CatalogApiService
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: CatalogApiService
) : CatalogRepository {

    override suspend fun getCategories(): Flow<List<Category>> = flow {
        try {
            val response = apiService.getCategories()
            val domainList = response.map { it.toDomain() }
            emit(domainList)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getProductsBySubCategory(subCategoryId: Long): Category {
        return apiService.getCategoryById(subCategoryId).toDomain()
    }


    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = apiService.searchProducts(query)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val productsDto = responseBody?.products
                val products = productsDto?.map { it.toDomain() } ?: emptyList()
                Result.success(products)
            } else {
                Result.failure(Exception("Error : ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

