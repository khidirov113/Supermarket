package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.BaseRepository
import com.example.supermarket.data.remote.network.StoreApi
import com.example.supermarket.domain.value.Store
import com.example.supermarket.domain.repository.StoreRepository
import jakarta.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val api: StoreApi
) : BaseRepository(), StoreRepository {
    override suspend fun getStores(): Result<List<Store>> {
        return safeApiCall { api.getStores() }.map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
}

