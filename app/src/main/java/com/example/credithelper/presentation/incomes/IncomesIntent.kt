package com.example.credithelper.presentation.incomes

import com.example.credithelper.domain.model.Income

sealed interface IncomesIntent {
    data object Load : IncomesIntent
    data object AddIncomeClicked : IncomesIntent
    data object DismissDialog : IncomesIntent
    data class SaveIncome(
        val name: String,
        val amount: Double,
        val isConfirmed: Boolean
    ) : IncomesIntent
    data class DeleteIncome(val id: Long) : IncomesIntent
}
