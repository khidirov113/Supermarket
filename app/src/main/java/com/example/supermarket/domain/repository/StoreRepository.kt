package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.Store

interface StoreRepository {
    suspend fun getStores(): Result<List<Store>>
}