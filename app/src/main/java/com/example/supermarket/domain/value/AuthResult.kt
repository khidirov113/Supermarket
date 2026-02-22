package com.example.supermarket.domain.value


data class AuthResult(
    val token: String?,
    val message: String? = null
)
