package com.example.supermarket.presentation.screen.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.domain.entity.AuthResult
import com.example.supermarket.domain.entity.UserProfile
import com.example.supermarket.domain.usecase.auth.SendCodeUseCase
import com.example.supermarket.domain.usecase.user.GetProfileUseCase
import com.example.supermarket.domain.usecase.user.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    val isAuthenticated = tokenManager.token.map { !it.isNullOrBlank() }

    var userData by mutableStateOf<UserProfile?>(null)

    init {
        viewModelScope.launch {
            isAuthenticated.collect { auth ->
                if (auth) {
                    fetchProfile()
                }
            }
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            getProfileUseCase().onSuccess { profile ->
                userData = profile
            }.onFailure {
                Log.d("SettingViewModel", "fetchProfile: FAILED. ${it.message}")
            }
        }
    }

    fun onLogout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                userData = null
                onSuccess()
            }
        }
    }
}