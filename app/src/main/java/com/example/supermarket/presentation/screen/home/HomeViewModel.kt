package com.example.supermarket.presentation.screen.home


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.data.local.TokenManager
import com.example.supermarket.domain.usecase.user.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    val isAuthenticated = tokenManager.token
        .map { !it.isNullOrBlank() }

        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _userBalance = mutableStateOf<String?>(null)
    val userBalance: State<String?> = _userBalance

    init {
        viewModelScope.launch {
            isAuthenticated.collect { auth ->
                if (auth) {
                    fetchBalance()
                } else {
                    _userBalance.value = null
                }
            }
        }
    }

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    fun fetchBalance() {
        viewModelScope.launch {
            getProfileUseCase()
                .onSuccess { user ->
                    _userBalance.value = "${user.bonus ?: 0.0} бон."
                }
                .onFailure { error ->
                    _errorEvents.emit("Error internet $error")
                }
        }
    }
}