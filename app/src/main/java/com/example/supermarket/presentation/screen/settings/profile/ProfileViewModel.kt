package com.example.supermarket.presentation.screen.settings.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.usecase.user.GetProfileUseCase
import com.example.supermarket.domain.usecase.user.LogoutUseCase
import com.example.supermarket.domain.usecase.user.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val surname: String = "",
    val bornIn: String = "",
    val gender: Int = 1,
    val imagePath: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        fetchCurrentProfile()
    }
    fun onNameChange(value: String) { uiState = uiState.copy(name = value) }
    fun onSurnameChange(value: String) { uiState = uiState.copy(surname = value) }
    fun onBornInChange(value: String) { uiState = uiState.copy(bornIn = value) }
    fun onGenderChange(value: Int) { uiState = uiState.copy(gender = value) }

    private fun fetchCurrentProfile() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            getProfileUseCase().onSuccess { user ->
                // Сервердан келган маълумотларни UI State-га ёзамиз
                uiState = uiState.copy(
                    name = user.firstName ?: "",
                    surname = user.lastName ?: "",
                    bornIn = user.birthDate ?: "",
                    gender = user.gender ?: 1,
                    isLoading = false
                )
            }.onFailure {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }
    fun updateProfile() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val result = updateProfileUseCase(
                name = uiState.name,
                surname = uiState.surname,
                bornIn = uiState.bornIn,
                gender = uiState.gender,
                imagePath = uiState.imagePath
            )
            result.onSuccess {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            }.onFailure { e ->
                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

}