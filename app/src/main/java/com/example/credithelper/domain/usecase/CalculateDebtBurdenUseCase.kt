package com.example.credithelper.domain.usecase

import com.example.credithelper.domain.model.BurdenLevel
import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.model.DebtBurden
import com.example.credithelper.domain.model.Income
import com.example.credithelper.domain.repository.IncomeRepository
import com.example.credithelper.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
/**
 * Calculates debt burden (DTI - Debt-to-Income) using bank formula:
 * 1. Sum confirmed annual incomes / 12 = average monthly income
 * 2. Sum all debt payments + planned loan payment = total monthly payments
 * 3. DTI% = (total monthly payments / avg monthly income) * 100
 */
class CalculateDebtBurdenUseCase constructor(
    private val incomeRepository: IncomeRepository,
    private val debtRepository: DebtRepository
) {
    operator fun invoke(plannedLoanPayment: Flow<Double>): Flow<DebtBurden> =
        combine(
            incomeRepository.getAllIncomes(),
            debtRepository.getAllDebts(),
            plannedLoanPayment
        ) { incomes, debts, planned ->
            calculate(incomes, debts, planned)
        }

    private fun calculate(
        incomes: List<Income>,
        debts: List<Debt>,
        plannedLoanPayment: Double
    ): DebtBurden {
        val confirmedIncomes = incomes.filter { it.isConfirmed }
        val annualIncome = if (confirmedIncomes.isEmpty()) {
            // If no confirmed income - use all incomes or regional average
            incomes.sumOf { it.amountMonthly * 12 }
        } else {
            confirmedIncomes.sumOf { it.amountMonthly * 12 }
        }
        val avgMonthlyIncome = if (annualIncome > 0) annualIncome / 12 else 1.0

        val existingPayments = debts.sumOf { it.monthlyPayment }
        val totalMonthlyPayments = existingPayments + plannedLoanPayment
        val totalRemainingToPay = debts.sumOf { it.remainingAmount }

        val dti = if (avgMonthlyIncome > 0) {
            (totalMonthlyPayments / avgMonthlyIncome) * 100
        } else 0.0

        val burdenLevel = BurdenLevel.fromPercentage(dti)
        val recommendations = getRecommendations(burdenLevel, dti, avgMonthlyIncome, totalMonthlyPayments)

        return DebtBurden(
            averageMonthlyIncome = avgMonthlyIncome,
            totalMonthlyPayments = totalMonthlyPayments,
            totalRemainingToPay = totalRemainingToPay,
            plannedLoanPayment = plannedLoanPayment,
            dtiPercentage = dti,
            burdenLevel = burdenLevel,
            recommendations = recommendations
        )
    }

    private fun getRecommendations(
        level: BurdenLevel,
        dti: Double,
        avgIncome: Double,
        totalPayments: Double
    ): List<String> = buildList {
        when (level) {
            BurdenLevel.LOW -> {
                add("✓ Ваша долговая нагрузка в норме")
                add("Можете рассматривать новые кредиты при необходимости")
                add("Рекомендуется формировать финансовую подушку")
            }
            BurdenLevel.MEDIUM -> {
                add("⚠ Оптимизируйте расходы по кредитам")
                add("Рассмотрите рефинансирование под меньший процент")
                add("По возможности досрочно погашайте самые дорогие кредиты")
                add("Не рекомендуется брать новые кредиты без крайней необходимости")
            }
            BurdenLevel.HIGH -> {
                add("‼ Срочно сократите долговую нагрузку")
                add("Обратитесь в банки для реструктуризации долгов")
                add("Рассмотрите объединение кредитов в один")
                add("Сократите расходы на необязательные траты")
                add("Ищите способы увеличения дохода")
                add("Новые кредиты противопоказаны")
            }
            BurdenLevel.CRITICAL -> {
                add("🚨 Критическая ситуация — требуется немедленное действие")
                add("Обратитесь в службу поддержки заёмщиков")
                add("Рассмотрите банкротство физлица как крайнюю меру")
                add("Составьте план постепенного погашения с приоритетом")
                add("Полностью исключите новые займы")
                add("Консультация финансового советника обязательна")
            }
        }
    }
}
