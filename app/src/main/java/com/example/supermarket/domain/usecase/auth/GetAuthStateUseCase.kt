package com.example.supermarket.domain.usecase.auth

import com.example.supermarket.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.token.map { token ->
            !token.isNullOrBlank()
        }
    }
}