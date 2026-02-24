package com.example.supermarket.presentation.screen.productlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.supermarket.domain.entity.Product
import com.example.supermarket.domain.usecase.category.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery
    private val _searchResult = MutableStateFlow<PagingData<Product>>(PagingData.empty())
    val searchResult: StateFlow<PagingData<Product>> = _searchResult.asStateFlow()

    private val _searchTotal = mutableStateOf(0)
    val searchTotal: State<Int> = _searchTotal

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isEmpty()) {
            searchJob?.cancel()
            _searchResult.value = PagingData.empty()
            _searchTotal.value = 0
        } else {
            searchProducts()
        }
    }

    private fun searchProducts() {
        val currentQuery = searchQuery.value
        if (currentQuery.isEmpty()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchProductsUseCase(currentQuery) { total ->
                _searchTotal.value = total
            }
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _searchResult.value = pagingData
                }
        }
    }
}