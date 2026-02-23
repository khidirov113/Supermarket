package com.example.supermarket.presentation.screen.productlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.category.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchResult = mutableStateOf<List<Product>>(emptyList())
    val searchResult: State<List<Product>> = _searchResult

    private val _searchTotal = mutableStateOf(0)
    val searchTotal: State<Int> = _searchTotal

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isEmpty()) {
            _searchResult.value = emptyList()
            _searchTotal.value = 0
            _isLoading.value = false
        } else {
            searchProducts()
        }
    }

    fun searchProducts() {
        val currentQuery = searchQuery.value
        if (currentQuery.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                searchProductsUseCase(currentQuery)
            }.onSuccess { data ->
                _searchResult.value = data.products
                _searchTotal.value = data.total
            }.onFailure { exception ->
                when (exception) {
                    is CancellationException -> throw exception
                    is AppException -> _errorMessage.value = exception.message
                    else -> _errorMessage.value = ": ${exception.message}"
                }
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}