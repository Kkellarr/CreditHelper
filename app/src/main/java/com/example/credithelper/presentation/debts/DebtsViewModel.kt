package com.example.credithelper.presentation.debts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.usecase.debt.AddDebtUseCase
import com.example.credithelper.domain.usecase.debt.DeleteDebtUseCase
import com.example.credithelper.domain.usecase.debt.GetDebtsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebtsViewModel(
    private val getDebtsUseCase: GetDebtsUseCase,
    private val addDebtUseCase: AddDebtUseCase,
    private val deleteDebtUseCase: DeleteDebtUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DebtsState())
    val state: StateFlow<DebtsState> = _state.asStateFlow()

    init {
        dispatch(DebtsIntent.Load)
    }

    fun dispatch(intent: DebtsIntent) {
        viewModelScope.launch {
            when (intent) {
                DebtsIntent.Load -> loadDebts()
                DebtsIntent.AddDebtClicked -> _state.update { it.copy(showAddDialog = true) }
                DebtsIntent.DismissDialog -> _state.update { it.copy(showAddDialog = false) }
                is DebtsIntent.SaveDebt -> saveDebt(intent)
                is DebtsIntent.DeleteDebt -> deleteDebt(intent.id)
            }
        }
    }

    private fun loadDebts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getDebtsUseCase()
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message ?: "Ошибка")
                    }
                }
                .collect { debts ->
                    _state.update {
                        it.copy(debts = debts, isLoading = false, error = null)
                    }
                }
        }
    }

    private suspend fun saveDebt(intent: DebtsIntent.SaveDebt) {
        addDebtUseCase(
            Debt(
                id = 0,
                name = intent.name,
                monthlyPayment = intent.monthlyPayment,
                totalAmount = intent.totalAmount,
                remainingAmount = intent.remainingAmount,
                type = intent.type
            )
        )
        _state.update { it.copy(showAddDialog = false) }
    }

    private suspend fun deleteDebt(id: Long) {
        deleteDebtUseCase(id)
    }
}
