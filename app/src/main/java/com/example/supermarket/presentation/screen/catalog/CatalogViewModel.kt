package com.example.supermarket.presentation.screen.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.category.GetCategoriesUseCase
import com.example.supermarket.domain.usecase.category.GetProductsBySubCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException // МУҲИМ!
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsBySubCategoryUseCase: GetProductsBySubCategoryUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<List<Category>>(emptyList())
    val categoriesState = _categoriesState.asStateFlow()

    private val _categoryByIdState = MutableStateFlow<Category?>(null)
    val categoryByIdState = _categoryByIdState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                getCategoriesUseCase().collect {
                    _categoriesState.value = it
                }
            }.onFailure { exception ->
                handleException(exception)
            }

            _isLoading.value = false
        }
    }

    fun loadProductsBySubCategory(subCategoryId: Long) {
        viewModelScope.launch {
            _categoryByIdState.value = null
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                val result = getProductsBySubCategoryUseCase(subCategoryId)
                _categoryByIdState.value = result
            }.onFailure { exception ->
                handleException(exception)
            }

            _isLoading.value = false
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is CancellationException -> throw exception
            is AppException -> _errorMessage.value = exception.message
            else -> _errorMessage.value = ": ${exception.message}"
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}