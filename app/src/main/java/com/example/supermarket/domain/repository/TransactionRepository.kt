package com.example.supermarket.domain.repository

import com.example.supermarket.domain.value.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>
    suspend fun getBonuses(): List<Transaction>
}