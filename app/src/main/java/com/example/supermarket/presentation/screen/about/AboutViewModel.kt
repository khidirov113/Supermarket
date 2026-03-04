package com.example.supermarket.presentation.screen.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.aboutusecase.GetAboutUseCase
import com.example.supermarket.domain.value.About
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val getAboutUseCase: GetAboutUseCase
) : ViewModel() {

    private val _about = MutableStateFlow<About?>(null)
    val about = _about.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMassage = MutableStateFlow<String?>(null)
    val errorMassage = _errorMassage.asStateFlow()

    fun loadAbout(id: Long) {
        if (_about.value?.id == id) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMassage.value = null
            runCatching {
                getAboutUseCase.invoke(id)
            }.onSuccess { about ->
                _about.value = about
            }.onFailure { exception ->
                when (exception) {
                    is CancellationException -> throw exception
                    is AppException -> _errorMassage.value = exception.message
                    else -> _errorMassage.value = "Unknow error ${exception.message}"
                }
            }
            _isLoading.value = false
        }
    }

    fun cleanError() {
        _errorMassage.value = null
    }
}
