package com.example.supermarket.domain.usecase.banner

import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.repository.BannerRepository
import javax.inject.Inject

class GetBannerByIdUseCase @Inject constructor(
    private val repository: BannerRepository
) {
    suspend operator fun invoke(id: Long): Banner {
        return repository.getBannerById(id)
    }

}