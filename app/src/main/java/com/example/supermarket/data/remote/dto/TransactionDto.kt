package com.example.supermarket.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val data: List<TransactionDto>
)

@Serializable
data class TransactionDto(
    val id: Long,
    val type: String? = null,
    val title: String? = null,
    val amount: Double? = null,
    val price: Double? = null,
    @SerialName("created_at") val createdAt: String? = null
)