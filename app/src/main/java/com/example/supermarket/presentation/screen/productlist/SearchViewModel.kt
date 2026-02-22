package com.example.supermarket.presentation.screen.productlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.usecase.category.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchResult = mutableStateOf<List<Product>>(emptyList())
    val searchResult: State<List<Product>> = _searchResult
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading



    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isEmpty()) {
            _searchResult.value = emptyList()
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
            val result = searchProductsUseCase(currentQuery)

            result.onSuccess { list ->
                _searchResult.value = list
            }

            _isLoading.value = false
        }
    }
}