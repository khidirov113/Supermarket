package com.example.supermarket.presentation.screen.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.user.GetProfileUseCase
import com.example.supermarket.domain.usecase.user.UpdateProfileUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.cancellation.CancellationException

data class ProfileUiState(
    val name: String = "",
    val surname: String = "",
    val bornIn: String = "",
    val gender: Int = 1,
    val selectedImageUri: Uri? = null,
    val imagePath: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        fetchCurrentProfile()
    }

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value)
    }

    fun onSurnameChange(value: String) {
        uiState = uiState.copy(surname = value)
    }

    fun onBornInChange(value: String) {
        uiState = uiState.copy(bornIn = value)
    }

    fun onGenderChange(value: Int) {
        uiState = uiState.copy(gender = value)
    }

    fun onImageSelected(uri: Uri) {
        uiState = uiState.copy(selectedImageUri = uri)
    }

    private fun fetchCurrentProfile() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            runCatching {
                getProfileUseCase()
            }.onSuccess { user ->
                uiState = uiState.copy(
                    name = user.firstName,
                    surname = user.lastName,
                    bornIn = user.birthDate,
                    gender = user.gender,
                    isLoading = false
                )
            }.onFailure { exception ->
                handleException(exception)
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun updateProfile(context: Context) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val imageFile = uiState.selectedImageUri?.let { uriToFile(context, it) }

            runCatching {
                updateProfileUseCase(
                    name = uiState.name,
                    surname = uiState.surname,
                    bornIn = uiState.bornIn,
                    gender = uiState.gender,
                    imagePath = imageFile?.absolutePath,
                )
            }.onSuccess {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            }.onFailure { exception ->
                handleException(exception)
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File(context.cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is CancellationException -> throw exception
            is AppException -> uiState = uiState.copy(errorMessage = exception.message)
            else -> uiState = uiState.copy(errorMessage = "Системная ошибка: ${exception.message}")
        }
    }

    fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }
}