package com.example.credithelper.presentation.debtdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credithelper.domain.usecase.debt.GetDebtByIdUseCase
import com.example.credithelper.domain.usecase.debt.UpdateDebtUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DebtDetailViewModel(
    private val debtId: Long,
    private val getDebtByIdUseCase: GetDebtByIdUseCase,
    private val updateDebtUseCase: UpdateDebtUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DebtDetailState())
    val state: StateFlow<DebtDetailState> = _state.asStateFlow()

    init {
        loadDebt()
    }

    fun dispatch(intent: DebtDetailIntent) {
        viewModelScope.launch {
            when (intent) {
                DebtDetailIntent.Load -> loadDebt()
                is DebtDetailIntent.UpdateName -> _state.update { it.copy(name = intent.name) }
                is DebtDetailIntent.UpdateAmount -> _state.update { it.copy(amountStr = intent.amount) }
                is DebtDetailIntent.UpdateTotalAmount -> _state.update { it.copy(totalAmountStr = intent.amount) }
                is DebtDetailIntent.UpdateRemainingAmount -> _state.update { it.copy(remainingAmountStr = intent.amount) }
                is DebtDetailIntent.UpdateType -> _state.update { it.copy(type = intent.type) }
                DebtDetailIntent.Save -> saveDebt()
            }
        }
    }

    private fun loadDebt() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val debt = getDebtByIdUseCase(debtId)
            _state.update {
                it.copy(
                    debt = debt,
                    name = debt?.name ?: "",
                    amountStr = debt?.let { d -> "%.0f".format(d.monthlyPayment) } ?: "",
                    totalAmountStr = debt?.let { d -> "%.0f".format(d.totalAmount) } ?: "",
                    remainingAmountStr = debt?.let { d -> "%.0f".format(d.remainingAmount) } ?: "",
                    type = debt?.type ?: com.example.credithelper.domain.model.DebtType.LOAN,
                    isLoading = false,
                    error = if (debt == null && debtId != 0L) "Кредит не найден" else null
                )
            }
        }
    }

    private suspend fun saveDebt() {
        val s = _state.value
        val debt = s.debt
        if (debt == null) {
            _state.update { it.copy(error = "Кредит не загружен. Попробуйте ещё раз.") }
            return
        }
        val amount = s.amountStr.toDoubleOrNull() ?: 0.0
        val totalAmount = s.totalAmountStr.toDoubleOrNull() ?: 0.0
        val remainingAmount = s.remainingAmountStr.toDoubleOrNull() ?: 0.0
        if (s.name.isBlank() || amount <= 0) {
            _state.update { it.copy(error = "Заполните все обязательные поля") }
            return
        }
        updateDebtUseCase(
            debt.copy(
                name = s.name,
                monthlyPayment = amount,
                totalAmount = totalAmount,
                remainingAmount = remainingAmount,
                type = s.type
            )
        )
        _state.update { it.copy(saved = true) }
    }
}
