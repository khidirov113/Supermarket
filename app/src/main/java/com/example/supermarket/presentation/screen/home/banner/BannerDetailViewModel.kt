package com.example.supermarket.presentation.screen.home.banner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Banner
import com.example.supermarket.domain.repository.BannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BannerDetailViewModel @Inject constructor(
    private val repository: BannerRepository
) : ViewModel() {

    private val _banner = MutableStateFlow<Banner?>(null)
    val banner = _banner.asStateFlow()

    fun loadBannerFromFlow(id: Long) {
        viewModelScope.launch {
            repository.getBanners().collect { list ->
                _banner.value = list.find { it.id == id }
            }
        }
    }
}