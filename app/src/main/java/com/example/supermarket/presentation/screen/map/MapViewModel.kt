package com.example.supermarket.presentation.screen.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Store
import com.example.supermarket.domain.usecase.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State


@HiltViewModel
class MapViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase
) : ViewModel() {

    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores = _stores.asStateFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadStores()
    }

    fun loadStores() {
        viewModelScope.launch {
            _isLoading.value = true
            getStoresUseCase().onSuccess { list ->
                _stores.value = list
            }
            _isLoading.value = false
        }
    }
}