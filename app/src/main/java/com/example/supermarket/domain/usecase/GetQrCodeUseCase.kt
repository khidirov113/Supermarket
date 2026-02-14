package com.example.supermarket.domain.usecase


import com.example.supermarket.domain.entity.QrCodeData
import com.example.supermarket.domain.repository.QrRepository
import javax.inject.Inject

class GetQrCodeUseCase @Inject constructor(
    private val repository: QrRepository
) {
    suspend operator fun invoke(): Result<QrCodeData> {
        return repository.getAccessCode()
    }
}

