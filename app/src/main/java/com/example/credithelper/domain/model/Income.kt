package com.example.credithelper.domain.model

/**
 * Domain model for income source.
 * @param id Unique identifier
 * @param name Source name (e.g. "Зарплата", "Фриланс")
 * @param amountMonthly Monthly amount in rubles
 * @param isConfirmed Whether income is documented/confirmed (bank requirement)
 */
data class Income(
    val id: Long,
    val name: String,
    val amountMonthly: Double,
    val isConfirmed: Boolean
)
