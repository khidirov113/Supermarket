package com.example.supermarket.presentation.screen.qrcode


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.usecase.qrcode.GetQrCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var timerJob: Job? = null

    fun loadQrCode() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            getQrCodeUseCase().onSuccess { data ->
                uiState = uiState.copy(code = data.code, isLoading = false)
                startTimer(60)
            }.onFailure { uiState = uiState.copy(isLoading = false) }
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
}