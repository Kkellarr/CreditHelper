package com.example.credithelper.presentation.incomedetail

sealed interface IncomeDetailIntent {
    data object Load : IncomeDetailIntent
    data class UpdateName(val name: String) : IncomeDetailIntent
    data class UpdateAmount(val amount: String) : IncomeDetailIntent
    data class UpdateIsConfirmed(val isConfirmed: Boolean) : IncomeDetailIntent
    data object Save : IncomeDetailIntent
}
