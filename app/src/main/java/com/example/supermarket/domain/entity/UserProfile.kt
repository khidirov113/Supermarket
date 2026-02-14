package com.example.supermarket.domain.entity

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val gender: Int,
    val image: String?,
    val bonus: Double?,
    val phone: String?
)
