package com.example.supermarket.presentation.screen.catalog


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.entity.Category
import com.example.supermarket.domain.usecase.category.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categoriesState = mutableStateOf<CatalogState>(CatalogState.Loading)
    val categoriesState: State<CatalogState> = _categoriesState

    init {
        loadCategories()
    }

    fun loadCategories() {
        _categoriesState.value = CatalogState.Loading
        viewModelScope.launch {
            getCategoriesUseCase().onSuccess { list ->
                _categoriesState.value = CatalogState.Success(list)
            }.onFailure { error ->
                _categoriesState.value = CatalogState.Error(error.message ?: "Unknown error")
            }
        }
    }
}

sealed class CatalogState {
    object Loading : CatalogState()
    data class Success(val categories: List<Category>) : CatalogState()
    data class Error(val message: String) : CatalogState()
}