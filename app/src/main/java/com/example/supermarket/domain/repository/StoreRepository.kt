package com.example.supermarket.domain.repository

import com.example.supermarket.domain.entity.Store

interface StoreRepository {
    suspend fun getStores(): Result<List<Store>>
}