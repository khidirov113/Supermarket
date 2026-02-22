package com.example.supermarket.presentation.screen.cardbanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.usecase.banner.GetBannersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val getBannersUseCase: GetBannersUseCase,
) : ViewModel() {

    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners = _banners.asStateFlow()

    init {
        fetchBanners()
    }

    fun fetchBanners() {
        viewModelScope.launch {
            delay(200)
            getBannersUseCase().collect {
                _banners.value = it
            }
        }
    }

}