package com.example.supermarket.domain.usecase.qrcode

import com.example.supermarket.domain.repository.StoreRepository
import com.example.supermarket.domain.value.Store
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    suspend operator fun invoke(): List<Store> = repository.getStores()
}