package com.example.supermarket.data.repository


import com.example.supermarket.data.mapper.toDomain
import com.example.supermarket.data.remote.network.TransactionApi
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.repository.TransactionRepository
import com.example.supermarket.domain.value.Transaction
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi
) : TransactionRepository {

    override suspend fun getTransactions(): List<Transaction> {
        return try {
            api.getTransactions().data.map { it.toDomain() }
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }

    override suspend fun getBonuses(): List<Transaction> {
        return try {
            api.getBonuses().data.map { it.toDomain() }
        } catch (e: IOException) {
            throw AppException.NetworkException()
        } catch (e: HttpException) {
            throw AppException.ServerException(e.code())
        } catch (e: Exception) {
            throw AppException.UnknownException()
        }
    }
}