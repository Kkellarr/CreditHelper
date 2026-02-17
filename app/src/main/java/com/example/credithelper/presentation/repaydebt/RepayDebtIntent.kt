package com.example.credithelper.presentation.repaydebt

sealed interface RepayDebtIntent {
    data class UpdateRepayAmount(val amount: String) : RepayDebtIntent
    data object Repay : RepayDebtIntent
    data object MinPaymentChanged : RepayDebtIntent
    data object MinPaymentUnchanged : RepayDebtIntent
    data class UpdateNewMinPayment(val amount: String) : RepayDebtIntent
    data object ConfirmAndSave : RepayDebtIntent
}
