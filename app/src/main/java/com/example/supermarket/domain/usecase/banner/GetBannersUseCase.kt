package com.example.supermarket.domain.usecase.banner

import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannersUseCase @Inject constructor(
    private val repository: BannerRepository
) {
    operator fun invoke(): Flow<List<Banner>> {
        return repository.getBanners()
    }

}