package com.example.supermarket.presentation.screen.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.usecase.category.GetCategoriesUseCase
import com.example.supermarket.domain.usecase.category.GetProductsBySubCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            delay(200)
            getCategoriesUseCase().collect {
                _categoriesState.value = it
            }
        }
    }

    fun loadProductsBySubCategory(subCategoryId: Long) {
        if (_categoryByIdState.value?.id == subCategoryId) return

        viewModelScope.launch {
            try {
                _categoryByIdState.value = getProductsBySubCategoryUseCase(subCategoryId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}