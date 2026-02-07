package com.example.credithelper.presentation.dashboard

sealed interface DashboardIntent {
    data object Load : DashboardIntent
    data object NavigateToIncomes : DashboardIntent
    data object NavigateToDebts : DashboardIntent
    data class UpdatePlannedLoan(val amount: Double) : DashboardIntent
}
