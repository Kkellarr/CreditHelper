package com.example.credithelper.domain.usecase.income

import com.example.credithelper.domain.repository.IncomeRepository
class DeleteIncomeUseCase constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteIncome(id)
}
