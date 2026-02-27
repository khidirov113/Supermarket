package com.example.supermarket.domain.value


data class Transaction(
    val id: Long,
    val title: String,
    val bonusAmount: Double,
    val price: Double,
    val date: String,
    val time: String,
    val isPositive: Boolean
)