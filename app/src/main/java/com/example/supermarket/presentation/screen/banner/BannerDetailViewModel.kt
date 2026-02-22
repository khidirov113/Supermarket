package com.example.supermarket.presentation.screen.banner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.usecase.banner.GetBannerByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BannerDetailViewModel @Inject constructor(
    private val getBannerByIdUseCase: GetBannerByIdUseCase
) : ViewModel() {

    private val _banner = MutableStateFlow<Banner?>(null)
    val banner = _banner.asStateFlow()

    fun loadBannerFromFlow(id: Long) {
        if (_banner.value?.id == id) return
        viewModelScope.launch {
            _banner.value = getBannerByIdUseCase(id)
        }
    }
}