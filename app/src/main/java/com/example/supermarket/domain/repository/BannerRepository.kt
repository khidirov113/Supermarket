package com.example.supermarket.domain.repository

import com.example.supermarket.domain.entity.Banner
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
     fun getBanners(): Flow<List<Banner>>
    suspend fun getBannerById(id: Long): Banner

}