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
    private val _state = MutableStateFlow<SettingState>(SettingState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            isAuthenticated.collect { auth ->
                if (auth) {
                    fetchProfile()
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
            getProfileUseCase().onSuccess { profile ->
                userData = profile
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

    fun processCommand(command: SettingCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingCommand.SetNotificationEnabled ->
                    updateNotificationsEnabledUseCase(command.enabled)
            }
        }
    }

}

sealed interface SettingCommand {
    data class SetNotificationEnabled(val enabled: Boolean) : SettingCommand
}

sealed interface SettingState {
    data object Initial : SettingState
    data class Configuration(val notificationsEnabled: Boolean) : SettingState
}