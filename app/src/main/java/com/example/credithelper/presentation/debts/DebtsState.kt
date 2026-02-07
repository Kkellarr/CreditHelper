package com.example.credithelper.presentation.debts

import com.example.credithelper.domain.model.Debt

data class DebtsState(
    val debts: List<Debt> = emptyList(),
    val showAddDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
