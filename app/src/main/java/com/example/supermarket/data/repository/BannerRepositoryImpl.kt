package com.example.supermarket.data.repository

import android.util.Log
import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.BannerApi
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerApi: BannerApi
) : BannerRepository {

    override fun getBanners(): Flow<List<Banner>> = flow {
        try {
            val response = bannerApi.getBanners()
            val domainList = response.map { it.toDomain() }
            emit(domainList)
            Log.d("BannerRepositoryImpl", "getBanners: $domainList")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getBannerById(id: Long): Banner {
        return bannerApi.getBannerById(id).toDomain()
    }
}
