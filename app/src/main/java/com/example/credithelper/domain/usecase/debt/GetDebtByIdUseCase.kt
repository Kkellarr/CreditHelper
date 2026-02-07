package com.example.credithelper.domain.usecase.debt

import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.repository.DebtRepository
class GetDebtByIdUseCase constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(id: Long): Debt? = repository.getDebtById(id)
}
