package com.example.supermarket.domain.usecase.qrcode


import com.example.supermarket.domain.value.QrCodeData
import com.example.supermarket.domain.repository.QrRepository
import javax.inject.Inject

class GetQrCodeUseCase @Inject constructor(
    private val repository: QrRepository
) {
    suspend operator fun invoke(): QrCodeData {
        return repository.getAccessCode()
    }
}

