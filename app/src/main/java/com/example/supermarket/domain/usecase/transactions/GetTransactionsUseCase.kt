package com.example.supermarket.domain.usecase.transactions


import com.example.supermarket.domain.repository.TransactionRepository
import com.example.supermarket.domain.value.Transaction
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): List<Transaction> = repository.getTransactions()
}