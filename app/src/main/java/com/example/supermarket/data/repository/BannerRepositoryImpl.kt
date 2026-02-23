package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.BannerApi
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerApi: BannerApi
) : BannerRepository {

    override fun getBanners(): Flow<List<Banner>> = flow {
        try {
            val response = bannerApi.getBanners()
            emit(response.map { it.toDomain() })
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        }

    }

    override suspend fun getBannerById(id: Long): Banner {
        return try {
            val response = bannerApi.getBannerById(id)
            response.toDomain()
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }
}
