package com.example.credithelper.presentation.incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credithelper.domain.model.Income
import com.example.credithelper.domain.usecase.income.AddIncomeUseCase
import com.example.credithelper.domain.usecase.income.DeleteIncomeUseCase
import com.example.credithelper.domain.usecase.income.GetIncomesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IncomesViewModel(
    private val getIncomesUseCase: GetIncomesUseCase,
    private val addIncomeUseCase: AddIncomeUseCase,
    private val deleteIncomeUseCase: DeleteIncomeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(IncomesState())
    val state: StateFlow<IncomesState> = _state.asStateFlow()

    init {
        dispatch(IncomesIntent.Load)
    }

    fun dispatch(intent: IncomesIntent) {
        viewModelScope.launch {
            when (intent) {
                IncomesIntent.Load -> loadIncomes()
                IncomesIntent.AddIncomeClicked -> _state.update { it.copy(showAddDialog = true) }
                IncomesIntent.DismissDialog -> _state.update { it.copy(showAddDialog = false) }
                is IncomesIntent.SaveIncome -> saveIncome(intent)
                is IncomesIntent.DeleteIncome -> deleteIncome(intent.id)
            }
        }
    }

    private fun loadIncomes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getIncomesUseCase()
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message ?: "Ошибка")
                    }
                }
                .collect { incomes ->
                    _state.update {
                        it.copy(incomes = incomes, isLoading = false, error = null)
                    }
                }
        }
    }

    private suspend fun saveIncome(intent: IncomesIntent.SaveIncome) {
        addIncomeUseCase(
            Income(
                id = 0,
                name = intent.name,
                amountMonthly = intent.amount,
                isConfirmed = intent.isConfirmed
            )
        )
        _state.update { it.copy(showAddDialog = false) }
    }

    private suspend fun deleteIncome(id: Long) {
        deleteIncomeUseCase(id)
    }
}
