package com.example.supermarket.presentation.screen.banner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.banner.GetBannerByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class BannerDetailViewModel @Inject constructor(
    private val getBannerByIdUseCase: GetBannerByIdUseCase
) : ViewModel() {

    private val _banner = MutableStateFlow<Banner?>(null)
    val banner = _banner.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMassage = MutableStateFlow<String?>(null)
    val errorMassage = _errorMassage.asStateFlow()

    fun loadBannerFromFlow(id: Long) {
        if (_banner.value?.id == id) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMassage.value = null
            runCatching {
                getBannerByIdUseCase(id)
            }.onSuccess { banner ->
                _banner.value = banner
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