package com.example.credithelper.domain.model

/**
 * Domain model for debt/loan.
 * @param id Unique identifier
 * @param name Debt name (e.g. "Ипотека", "Кредитная карта")
 * @param monthlyPayment Monthly payment amount in rubles
 * @param totalAmount Полное тело кредита (сумма займа) в рублях
 * @param remainingAmount Остаток к выплате в рублях
 * @param type Debt type - LOAN or CREDIT_CARD
 */
data class Debt(
    val id: Long,
    val name: String,
    val monthlyPayment: Double,
    val totalAmount: Double,
    val remainingAmount: Double,
    val type: DebtType
)

enum class DebtType {
    LOAN,       // Кредит
    CREDIT_CARD // Кредитная карта
}
