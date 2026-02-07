package com.example.credithelper.domain.usecase.debt

import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.repository.DebtRepository
class UpdateDebtUseCase constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(debt: Debt) = repository.updateDebt(debt)
}
