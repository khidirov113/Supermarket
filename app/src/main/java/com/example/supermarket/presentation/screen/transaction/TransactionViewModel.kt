package com.example.supermarket.presentation.screen.transaction


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.domain.error.AppException
import com.example.supermarket.domain.usecase.transactions.GetBonusesUseCase
import com.example.supermarket.domain.usecase.transactions.GetTransactionsUseCase
import com.example.supermarket.domain.value.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

data class TransactionState(
    val isLoading: Boolean = false,
    val purchases: List<Transaction> = emptyList(),
    val bonuses: List<Transaction> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getBonusesUseCase: GetBonusesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state = _state.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val purchasesDeferred = async { getTransactionsUseCase() }
                val bonusesDeferred = async { getBonusesUseCase() }

                val purchasesResult = purchasesDeferred.await()
                val bonusesResult = bonusesDeferred.await()

                _state.update {
                    it.copy(
                        isLoading = false,
                        purchases = purchasesResult,
                        bonuses = bonusesResult
                    )
                }
            } catch (exception: Throwable) {
                when (exception) {
                    is CancellationException -> throw exception
                    is AppException -> _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }

                    else -> _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Ошибка сервера: ${exception.message}"
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}