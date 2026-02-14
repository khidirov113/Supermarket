package com.example.supermarket.presentation.utils

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.usecase.auth.SendCodeUseCase
import com.example.supermarket.domain.usecase.auth.VerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendCodeUseCase: SendCodeUseCase,
    private val verifySmsUseCase: VerifyCodeUseCase
) : ViewModel() {

    var step by mutableIntStateOf(1)
    var phoneInput by mutableStateOf("")
    var code by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)


    var timerSeconds by mutableIntStateOf(60)
    private var timerJob: Job? = null

    val isPhoneValid: Boolean get() = phoneInput.length == 9

    fun startTimer() {
        timerSeconds = 60
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerSeconds > 0) {
                delay(1000L)
                timerSeconds--
            }
        }
    }

    fun onSendCodeClick() {
        if (!isPhoneValid) return

        viewModelScope.launch {
            isLoading = true
            val fullPhone = "992$phoneInput" // Серверга 992 билан юборамиз

            sendCodeUseCase(fullPhone).onSuccess {
                step = 2
                startTimer()
            }.onFailure { e ->
                errorMessage = "Хатолик юз берди"
                Log.e("AUTH", "Error: ${e.message}")
            }
            isLoading = false
        }
    }

    fun onVerifyCodeClick(onSuccess: () -> Unit) {
        if (code.length < 4) return

        viewModelScope.launch {
            isLoading = true
            val fullPhone = "992$phoneInput"
            verifySmsUseCase(fullPhone, code).onSuccess {
                onSuccess()
            }.onFailure {
                errorMessage = "Код нотўғри"
            }
            isLoading = false
        }
    }
}