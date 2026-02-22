package com.example.supermarket.domain.value

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val gender: Int,
    val image: String?,
    val bonus: Double?,
    val phone: String?
)