package com.example.credithelper.presentation.incomedetail

import com.example.credithelper.domain.model.Income

data class IncomeDetailState(
    val income: Income? = null,
    val name: String = "",
    val amountStr: String = "",
    val isConfirmed: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)
