package com.example.credithelper.presentation.debtdetail

import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.model.DebtType

data class DebtDetailState(
    val debt: Debt? = null,
    val name: String = "",
    val amountStr: String = "",
    val totalAmountStr: String = "",
    val remainingAmountStr: String = "",
    val type: DebtType = DebtType.LOAN,
    val isLoading: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)
