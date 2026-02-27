package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.StoreApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.repository.StoreRepository
import com.example.supermarket.domain.value.Store
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException

class StoreRepositoryImpl @Inject constructor(
    private val api: StoreApi
) : StoreRepository {
    override suspend fun getStores(): List<Store> {
        return try {
            api.getStores().map { it.toDomain() }
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }
}

