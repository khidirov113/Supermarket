package com.example.supermarket.presentation.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.value.Notification
import com.example.supermarket.domain.usecase.notification.GetNewsUseCase
import com.example.supermarket.domain.usecase.notification.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import javax.inject.Inject

data class NotificationState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val news: List<Notification> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    init {
        loadNotifications()
        loadNews()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                getNotificationsUseCase()
            }.onSuccess { data ->
                _state.update { it.copy(isLoading = false, notifications = data) }
            }.onFailure { exception ->
                handleException(exception)
            }
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                getNewsUseCase()
            }.onSuccess { data ->
                _state.update { it.copy(isLoading = false, news = data) }
            }.onFailure { exception ->
                handleException(exception)
            }
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is CancellationException -> throw exception
            is AppException -> _state.update {
                it.copy(isLoading = false, errorMessage = exception.message)
            }

            else -> _state.update {
                it.copy(isLoading = false, errorMessage = "Ошибка: ${exception.message}")
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}