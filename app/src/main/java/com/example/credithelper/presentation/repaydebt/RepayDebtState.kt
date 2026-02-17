package com.example.credithelper.presentation.repaydebt

import com.example.credithelper.domain.model.Debt

data class RepayDebtState(
    val debt: Debt? = null,
    val repayAmountStr: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false,
    /** После успешного погашения: спрашиваем, изменился ли мин. платёж */
    val askingMinPaymentChange: Boolean = false,
    /** Пользователь ответил «да», показываем поле для нового мин. платежа */
    val minPaymentChanged: Boolean = false,
    val newMinPaymentStr: String = ""
)
