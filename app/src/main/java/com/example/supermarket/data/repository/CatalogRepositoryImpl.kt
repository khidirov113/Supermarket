package com.example.supermarket.data.repository


import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.CatalogApiService
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.entity.SearchProductsResult
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: CatalogApiService
) : CatalogRepository {

    override suspend fun getCategories(): Flow<List<Category>> = flow {
        try {
            val response = apiService.getCategories()
            emit(response.map { it.toDomain() })
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        }
    }

    override suspend fun getCategoryDetail(subCategoryId: Long): Category {
        return try {
            val response = apiService.getCategoryById(subCategoryId)
            response.toDomain()
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        }
    }

    override suspend fun searchProducts(query: String): SearchProductsResult {
        return try {
            val response = apiService.searchProducts(query)
            SearchProductsResult(
                products = response.products.map { it.toDomain() },
                total = response.total
            )
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        }
    }
}

