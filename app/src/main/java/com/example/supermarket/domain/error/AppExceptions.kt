package com.example.supermarket.domain.error

sealed class AppExceptions(message: String = "") : RuntimeException() {
    class ErrorNetworkException : AppExceptions("Not found")
}