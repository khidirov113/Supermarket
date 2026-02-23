package com.example.supermarket.domain.error

sealed class AppException(massage: String) : RuntimeException(massage) {

    class NetworkException : AppException("Не удается подключиться к интернету")

    class ServerException(val code: Int) : AppException("Ошибка сервера: $code")
    class UnknownException(msg: String = "Произошла неизвестная ошибка") : AppException(msg)

    class InvalidPhoneException : AppException("Неверный номер телефона")
    class InvalidCodeException : AppException("Неверный код подтверждения")
    class TooManyRequestsException : AppException("Слишком много попыток. Пожалуйста, подождите немного.")}