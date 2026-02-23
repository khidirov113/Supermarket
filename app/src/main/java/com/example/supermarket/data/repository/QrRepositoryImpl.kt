package com.example.supermarket.data.repository

import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.QrApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.value.QrCodeData
import com.example.supermarket.domain.repository.QrRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(
    private val api: QrApi
) : QrRepository {


    override suspend fun getAccessCode(): QrCodeData {
        return try {
            api.getAccessCode().toDomain()
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun verifyAccessCode(code: Int) {
        try {
            api.verifyAccessCode(mapOf("code" to code))
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }
}