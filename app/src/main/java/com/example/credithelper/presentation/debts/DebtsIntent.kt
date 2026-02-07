package com.example.credithelper.presentation.debts

import com.example.credithelper.domain.model.DebtType

sealed interface DebtsIntent {
    data object Load : DebtsIntent
    data object AddDebtClicked : DebtsIntent
    data object DismissDialog : DebtsIntent
    data class SaveDebt(
        val name: String,
        val monthlyPayment: Double,
        val totalAmount: Double,
        val remainingAmount: Double,
        val type: DebtType
    ) : DebtsIntent
    data class DeleteDebt(val id: Long) : DebtsIntent
}
