package com.example.credithelper.domain.model

/**
 * Result of debt burden calculation.
 * Formula: DTI = (totalMonthlyPayments / avgMonthlyIncome) * 100%
 */
data class DebtBurden(
    val averageMonthlyIncome: Double,
    val totalMonthlyPayments: Double,
    val totalRemainingToPay: Double,
    val plannedLoanPayment: Double,
    val dtiPercentage: Double,
    val burdenLevel: BurdenLevel,
    val recommendations: List<String>
)

/**
 * Debt burden level based on DTI percentage.
 * Banks typically use: <30% low, 30-50% medium, 50-70% high, >70% critical
 */
enum class BurdenLevel(
    val displayName: String,
    val minPercent: Double,
    val maxPercent: Double,
    val colorRes: Long,
    val description: String
) {
    LOW(
        displayName = "Низкая",
        minPercent = 0.0,
        maxPercent = 30.0,
        colorRes = 0xFF4CAF50,
        description = "Хорошее финансовое здоровье"
    ),
    MEDIUM(
        displayName = "Средняя",
        minPercent = 30.0,
        maxPercent = 50.0,
        colorRes = 0xFFFFC107,
        description = "Рекомендуется оптимизация"
    ),
    HIGH(
        displayName = "Высокая",
        minPercent = 50.0,
        maxPercent = 70.0,
        colorRes = 0xFFFF9800,
        description = "Требуются срочные меры"
    ),
    CRITICAL(
        displayName = "Критическая",
        minPercent = 70.0,
        maxPercent = Double.MAX_VALUE,
        colorRes = 0xFFF44336,
        description = "Необходима серьёзная реструктуризация"
    );

    companion object {
        fun fromPercentage(dti: Double): BurdenLevel = entries.find {
            dti >= it.minPercent && dti < it.maxPercent
        } ?: CRITICAL
    }
}
