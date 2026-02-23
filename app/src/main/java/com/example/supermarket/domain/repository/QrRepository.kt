package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.QrCodeData

interface QrRepository {

    suspend fun getAccessCode(): QrCodeData

    suspend fun verifyAccessCode(code: Int)
}