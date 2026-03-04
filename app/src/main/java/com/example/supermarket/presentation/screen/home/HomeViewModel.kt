package com.example.supermarket.presentation.screen.home


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.domain.usecase.user.GetProfileUseCase
import com.example.supermarket.domain.usecase.user.UpdateFcmTokenUseCase
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase
) : ViewModel() {

    val isAuthenticated = tokenManager.token
        .map { !it.isNullOrBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _userBalance = mutableStateOf<String?>(null)
    val userBalance: State<String?> = _userBalance

    init {
        viewModelScope.launch {
            isAuthenticated.collect { auth ->
                if (auth == true) {
                    fetchBalance()
                } else if (auth == false){
                    _userBalance.value = null
                }
            }
        }
    }
    init {
        viewModelScope.launch {
            isAuthenticated.collect { isAuth ->
                if (isAuth == true) {
                    sendFcmTokenToServer()
                }
            }
        }
    }

    private fun sendFcmTokenToServer() {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                viewModelScope.launch {
                    runCatching {
                        updateFcmTokenUseCase(token)
                    }.onSuccess {
                        Log.d("FCM", "Get Token $token ")
                    }.onFailure {
                        Log.d("FCM", "Token error: ${it.message}")
                    }
                }
            }
        }
    }

    fun fetchBalance() {
        viewModelScope.launch {
            _userBalance.value = null

            runCatching {
                getProfileUseCase()
            }.onSuccess { user ->
                _userBalance.value = "${user.bonus ?: 0.0} бон."
            }.onFailure { exception ->
                if (exception !is CancellationException) {
                    _userBalance.value = " "
                }
            }
        }
    }
}