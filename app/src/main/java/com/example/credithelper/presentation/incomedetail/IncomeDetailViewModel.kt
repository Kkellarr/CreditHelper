package com.example.credithelper.presentation.incomedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credithelper.domain.usecase.income.GetIncomeByIdUseCase
import com.example.credithelper.domain.usecase.income.UpdateIncomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IncomeDetailViewModel(
    private val incomeId: Long,
    private val getIncomeByIdUseCase: GetIncomeByIdUseCase,
    private val updateIncomeUseCase: UpdateIncomeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(IncomeDetailState())
    val state: StateFlow<IncomeDetailState> = _state.asStateFlow()

    init {
        loadIncome()
    }

    fun dispatch(intent: IncomeDetailIntent) {
        viewModelScope.launch {
            when (intent) {
                IncomeDetailIntent.Load -> loadIncome()
                is IncomeDetailIntent.UpdateName -> _state.update { it.copy(name = intent.name) }
                is IncomeDetailIntent.UpdateAmount -> _state.update { it.copy(amountStr = intent.amount) }
                is IncomeDetailIntent.UpdateIsConfirmed -> _state.update { it.copy(isConfirmed = intent.isConfirmed) }
                IncomeDetailIntent.Save -> saveIncome()
            }
        }
    }

    private fun loadIncome() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val income = getIncomeByIdUseCase(incomeId)
            _state.update {
                it.copy(
                    income = income,
                    name = income?.name ?: "",
                    amountStr = income?.let { i -> "%.0f".format(i.amountMonthly) } ?: "",
                    isConfirmed = income?.isConfirmed ?: true,
                    isLoading = false,
                    error = if (income == null && incomeId != 0L) "Доход не найден" else null
                )
            }
        }
    }

    private suspend fun saveIncome() {
        val s = _state.value
        val income = s.income
        if (income == null) {
            _state.update { it.copy(error = "Доход не загружен. Попробуйте ещё раз.") }
            return
        }
        val amount = s.amountStr.toDoubleOrNull() ?: 0.0
        if (s.name.isBlank() || amount <= 0) {
            _state.update { it.copy(error = "Заполните все поля") }
            return
        }
        updateIncomeUseCase(
            income.copy(
                name = s.name,
                amountMonthly = amount,
                isConfirmed = s.isConfirmed
            )
        )
        _state.update { it.copy(saved = true) }
    }
}
