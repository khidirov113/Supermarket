package com.example.supermarket.presentation.screen.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.product.GetWeekSalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getWeekSalesUseCase: GetWeekSalesUseCase
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMassage = MutableStateFlow<String?>(null)
    val errorMassage = _errorMassage.asStateFlow()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMassage.value = null
            runCatching {
                getWeekSalesUseCase().collect { productList ->
                    _products.value = productList
                }
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