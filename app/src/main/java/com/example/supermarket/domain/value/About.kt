package com.example.supermarket.domain.value

data class About(
    val id : Long,
    val title: String,
    val description: String,
    val phone: String,
    val telegram: String,
    val whatsapp: String,
    val images: String?
)
