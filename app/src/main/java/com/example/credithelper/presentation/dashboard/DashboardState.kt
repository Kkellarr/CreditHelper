package com.example.credithelper.presentation.dashboard

import com.example.credithelper.domain.model.DebtBurden

data class DashboardState(
    val debtBurden: DebtBurden? = null,
    val plannedLoanPayment: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
