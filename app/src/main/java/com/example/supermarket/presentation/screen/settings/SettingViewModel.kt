package com.example.supermarket.presentation.screen.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.value.UserProfile
import com.example.supermarket.domain.usecase.setting.GetSettingUseCase
import com.example.supermarket.domain.usecase.setting.UpdateNotificationsEnabledUseCase
import com.example.supermarket.domain.usecase.user.GetProfileUseCase
import com.example.supermarket.domain.usecase.user.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    getSettingUseCase: GetSettingUseCase
) : ViewModel() {

    val isAuthenticated = tokenManager.token.map { !it.isNullOrBlank() }

    var userData by mutableStateOf<UserProfile?>(null)
        private set

    private val _state = MutableStateFlow<SettingState>(SettingState.Configuration(false))
    val state = _state.asStateFlow()

    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            isAuthenticated.collect { auth ->
                if (auth) {
                    fetchProfile()
                } else {
                    userData = null
                }
            }
        }

        getSettingUseCase()
            .onEach { settings ->
                _state.update {
                    SettingState.Configuration(notificationsEnabled = settings.notificationsEnabled)
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            runCatching {
                getProfileUseCase()
            }.onSuccess { profile ->
                userData = profile
            }.onFailure { exception ->
                handleException(exception)
            }

            isLoading = false
        }
    }

    fun onLogout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // ТЎҒИРЛАНДИ: runCatching га ўтказилди
            runCatching {
                logoutUseCase()
            }.onSuccess {
                userData = null
                onSuccess()
            }.onFailure { exception ->
                handleException(exception)
            }

            isLoading = false
        }
    }

    fun processCommand(command: SettingCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingCommand.SetNotificationEnabled ->
                    updateNotificationsEnabledUseCase(command.enabled)
            }
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is CancellationException -> throw exception
            is AppException -> errorMessage = exception.message
            else -> errorMessage = "Ошибка: ${exception.message}"
        }
    }

    fun clearError() {
        errorMessage = null
    }
}

sealed interface SettingCommand {
    data class SetNotificationEnabled(val enabled: Boolean) : SettingCommand
}

sealed interface SettingState {
    data object Initial : SettingState
    data class Configuration(val notificationsEnabled: Boolean) : SettingState
}