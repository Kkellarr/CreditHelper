package com.example.credithelper.presentation.repaydebt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credithelper.domain.usecase.debt.GetDebtByIdUseCase
import com.example.credithelper.domain.usecase.debt.UpdateDebtUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepayDebtViewModel(
    private val debtId: Long,
    private val getDebtByIdUseCase: GetDebtByIdUseCase,
    private val updateDebtUseCase: UpdateDebtUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RepayDebtState())
    val state: StateFlow<RepayDebtState> = _state.asStateFlow()

    init {
        loadDebt()
    }

    fun dispatch(intent: RepayDebtIntent) {
        viewModelScope.launch {
            when (intent) {
                is RepayDebtIntent.UpdateRepayAmount -> _state.update { it.copy(repayAmountStr = intent.amount) }
                RepayDebtIntent.Repay -> repay()
                RepayDebtIntent.MinPaymentChanged -> _state.update {
                    it.copy(askingMinPaymentChange = false, minPaymentChanged = true)
                }
                RepayDebtIntent.MinPaymentUnchanged -> saveWithoutMinPaymentChange()
                is RepayDebtIntent.UpdateNewMinPayment -> _state.update { it.copy(newMinPaymentStr = intent.amount) }
                RepayDebtIntent.ConfirmAndSave -> saveWithNewMinPayment()
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
                    isLoading = false,
                    error = if (debt == null && debtId != 0L) "Кредит не найден" else null
                )
            }
        }
    }

    private suspend fun repay() {
        val s = _state.value
        val debt = s.debt ?: run {
            _state.update { it.copy(error = "Кредит не загружен") }
            return
        }
        val repayAmount = s.repayAmountStr.toDoubleOrNull() ?: 0.0
        if (repayAmount <= 0) {
            _state.update { it.copy(error = "Введите сумму погашения") }
            return
        }
        if (repayAmount > debt.remainingAmount) {
            _state.update { it.copy(error = "Сумма не может быть больше остатка (${debt.remainingAmount.toLong()} ₽)") }
            return
        }
        _state.update { it.copy(error = null) }
        // Уменьшаем остаток к выплате (тело кредита)
        val newRemaining = (debt.remainingAmount - repayAmount).coerceAtLeast(0.0)
        val updatedDebt = debt.copy(remainingAmount = newRemaining)
        updateDebtUseCase(updatedDebt)
        _state.update {
            it.copy(
                debt = updatedDebt,
                repayAmountStr = "",
                askingMinPaymentChange = true
            )
        }
    }

    private suspend fun saveWithoutMinPaymentChange() {
        _state.update { it.copy(saved = true) }
    }

    private suspend fun saveWithNewMinPayment() {
        val s = _state.value
        val debt = s.debt ?: run {
            _state.update { it.copy(saved = true) }
            return
        }
        val newMinPayment = s.newMinPaymentStr.toDoubleOrNull() ?: 0.0
        if (newMinPayment <= 0) {
            _state.update { it.copy(error = "Введите новый минимальный платёж") }
            return
        }
        updateDebtUseCase(debt.copy(monthlyPayment = newMinPayment))
        _state.update { it.copy(saved = true) }
    }
}
