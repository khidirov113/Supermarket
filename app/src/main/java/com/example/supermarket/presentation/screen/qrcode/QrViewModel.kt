package com.example.supermarket.presentation.screen.qrcode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.qrcode.GetQrCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import javax.inject.Inject

data class QrUiState(
    val isLoading: Boolean = false,
    val code: String? = null,
    val secondsRemaining: Int = 60,
    val error: String? = null
)

@HiltViewModel
class QrViewModel @Inject constructor(
    private val getQrCodeUseCase: GetQrCodeUseCase
) : ViewModel() {

    var uiState by mutableStateOf(QrUiState())
        private set

    private var timerJob: Job? = null

    fun loadQrCode() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            runCatching {
                getQrCodeUseCase()
            }.onSuccess { data ->
                uiState = uiState.copy(code = data.code, isLoading = false)
                startTimer(60)
            }.onFailure { exception ->
                handleException(exception)
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var current = seconds
            while (current > 0) {
                uiState = uiState.copy(secondsRemaining = current)
                delay(1000)
                current--
            }
            loadQrCode()
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is CancellationException -> throw exception
            is AppException -> uiState = uiState.copy(error = exception.message)
            else -> uiState = uiState.copy(error = "Ошибка: ${exception.message}")
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}