package com.example.supermarket.domain.usecase.qrcode

import com.example.supermarket.domain.repository.StoreRepository
import jakarta.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    suspend operator fun invoke() = repository.getStores()
}