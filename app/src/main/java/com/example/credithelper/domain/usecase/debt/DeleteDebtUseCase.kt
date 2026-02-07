package com.example.credithelper.domain.usecase.debt

import com.example.credithelper.domain.repository.DebtRepository
class DeleteDebtUseCase constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteDebt(id)
}
