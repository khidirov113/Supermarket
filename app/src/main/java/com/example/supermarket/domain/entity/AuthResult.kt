package com.example.supermarket.domain.entity


data class AuthResult(
    val token: String?,
    val message: String? = null
)
