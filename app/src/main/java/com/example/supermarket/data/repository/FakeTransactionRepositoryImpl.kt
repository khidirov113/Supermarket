package com.example.supermarket.data.repository


import com.example.supermarket.domain.repository.TransactionRepository
import com.example.supermarket.domain.value.Transaction
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeTransactionRepositoryImpl @Inject constructor() : TransactionRepository {

    override suspend fun getTransactions(): List<Transaction> {
        delay(1000)

        return listOf(
            Transaction(
                id = 1,
                title = "Начисление",
                bonusAmount = 10.00,
                price = 250.0,
                date = "04.08.2024",
                time = "20:16",
                isPositive = true
            ),
            Transaction(
                id = 2,
                title = "Снятие",
                bonusAmount = 10.00,
                price = 200.0,
                date = "04.08.2024",
                time = "18:10",
                isPositive = false
            ),
            Transaction(
                id = 3,
                title = "Снятие",
                bonusAmount = 10.00,
                price = 200.0,
                date = "03.08.2024",
                time = "18:10",
                isPositive = false
            ),
            Transaction(
                id = 4,
                title = "Начисление",
                bonusAmount = 10.00,
                price = 250.0,
                date = "03.08.2024",
                time = "20:16",
                isPositive = true
            ),
            Transaction(
                id = 5,
                title = "Снятие",
                bonusAmount = 10.00,
                price = 200.0,
                date = "03.08.2024",
                time = "18:10",
                isPositive = false
            )
        )
    }

    override suspend fun getBonuses(): List<Transaction> {
        delay(1000)

        return listOf(
            Transaction(
                id = 6,
                title = "Начисление",
                bonusAmount = 10.00,
                price = 250.0,
                date = "04.08.2024",
                time = "20:16",
                isPositive = true
            ),
            Transaction(
                id = 7,
                title = "Снятие",
                bonusAmount = 12.00,
                price = 220.0,
                date = "04.08.2024",
                time = "18:10",
                isPositive = true
            ),
            Transaction(
                id = 8,
                title = "Снятие",
                bonusAmount = 20.00,
                price = 230.0,
                date = "03.08.2024",
                time = "18:10",
                isPositive = true
            )
        )
    }
}