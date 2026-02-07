package com.example.credithelper.presentation.dashboard

sealed interface DashboardEffect {
    data object NavigateToIncomes : DashboardEffect
    data object NavigateToDebts : DashboardEffect
}
