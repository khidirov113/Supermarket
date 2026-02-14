package com.example.supermarket.domain.repository

import com.example.supermarket.domain.entity.QrCodeData

interface QrRepository {

    suspend fun getAccessCode(): Result<QrCodeData>

    suspend fun verifyAccessCode(code: Int): Result<Unit>
}