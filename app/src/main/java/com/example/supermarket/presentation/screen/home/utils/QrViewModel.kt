package com.example.supermarket.presentation.screen.home.utils


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.usecase.GetQrCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QrUiState(
    val isLoading: Boolean = false,
    val code: String? = null,
    val secondsRemaining: Int = 60, // Таймер учун
    val error: String? = null
)

@HiltViewModel
class QrViewModel @Inject constructor(
    private val getQrCodeUseCase: GetQrCodeUseCase
) : ViewModel() {
    var uiState by mutableStateOf(QrUiState())
    private var timerJob: kotlinx.coroutines.Job? = null

    fun loadQrCode() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            getQrCodeUseCase().onSuccess { data ->
                uiState = uiState.copy(code = data.code, isLoading = false)
                startTimer(60) // 60 сониялик таймерни бошлаш
            }.onFailure { uiState = uiState.copy(isLoading = false) }
        }
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var current = seconds
            while (current > 0) {
                uiState = uiState.copy(secondsRemaining = current)
                kotlinx.coroutines.delay(1000)
                current--
            }
            loadQrCode() // Таймер тугаса кодни янгилаш
        }
    }
}