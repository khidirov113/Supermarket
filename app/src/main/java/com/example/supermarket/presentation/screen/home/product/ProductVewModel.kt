package com.example.supermarket.presentation.screen.home.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.usecase.product.GetWeekSalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductVewModel @Inject constructor(
    private val getWeekSalesUseCase: GetWeekSalesUseCase
): ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            getWeekSalesUseCase().collect {
                _products.value = it
            }
        }
    }

}