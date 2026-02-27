package com.example.supermarket.data.remote.network


import com.example.supermarket.data.remote.dto.TransactionResponse
import retrofit2.http.GET

interface TransactionApi {
    @GET("/api/transactions")
    suspend fun getTransactions(): TransactionResponse

    @GET("/api/bonuses")
    suspend fun getBonuses(): TransactionResponse
}