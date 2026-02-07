package com.example.credithelper.presentation.incomes

import com.example.credithelper.domain.model.Income

data class IncomesState(
    val incomes: List<Income> = emptyList(),
    val showAddDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
