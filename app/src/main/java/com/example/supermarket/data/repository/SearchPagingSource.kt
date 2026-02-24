package com.example.supermarket.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.CatalogApiService
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.error.AppException
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val apiService: CatalogApiService,
    private val query: String,
    private val onTotalCount: (Int) -> Unit
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        return try {

            val response = apiService.searchProducts(query, page)

            if (page == 1) {
                onTotalCount(response.total)
            }

            val products = response.products.map { it.toDomain() }

            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(AppException.NetworkException())
        } catch (e: HttpException) {
            LoadResult.Error(AppException.ServerException(e.code()))
        } catch (e: Exception) {
            LoadResult.Error(AppException.UnknownException(e.message ?: "Unknown error"))
        }
    }
}