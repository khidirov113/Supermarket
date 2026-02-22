package com.example.supermarket.presentation.screen.productdelail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.usecase.product.GetProductByIdUseCase
import com.example.supermarket.domain.usecase.product.GetSimilarProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getSimilarProductsUseCase: GetSimilarProductsUseCase
) : ViewModel() {

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()
    private val _similarProducts = MutableStateFlow<List<Product>>(emptyList())
    val similarProducts: StateFlow<List<Product>> = _similarProducts.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    fun loadProductDetails(productId: Long) {
        if (_selectedProduct.value?.id == productId && _similarProducts.value.isNotEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true

            _similarProducts.value = emptyList()

            try {
                val product = getProductByIdUseCase(productId)
                _selectedProduct.value = product

                if (product != null) {
                    val similar = getSimilarProductsUseCase(productId)
                    _similarProducts.value = similar
                }
            } catch (e: Exception) {
                _similarProducts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}