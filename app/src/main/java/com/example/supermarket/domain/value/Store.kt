package com.example.supermarket.domain.value

data class Store(
    val id: Int,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val fromTime: String,
    val toTime: String,
    val images: List<String>
)