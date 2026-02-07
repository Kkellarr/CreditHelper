package com.example.credithelper.presentation.debtdetail

import com.example.credithelper.domain.model.DebtType

sealed interface DebtDetailIntent {
    data object Load : DebtDetailIntent
    data class UpdateName(val name: String) : DebtDetailIntent
    data class UpdateAmount(val amount: String) : DebtDetailIntent
    data class UpdateTotalAmount(val amount: String) : DebtDetailIntent
    data class UpdateRemainingAmount(val amount: String) : DebtDetailIntent
    data class UpdateType(val type: DebtType) : DebtDetailIntent
    data object Save : DebtDetailIntent
}
