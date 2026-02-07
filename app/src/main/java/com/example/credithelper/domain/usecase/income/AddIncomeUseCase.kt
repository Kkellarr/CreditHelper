package com.example.credithelper.domain.usecase.income

import com.example.credithelper.domain.model.Income
import com.example.credithelper.domain.repository.IncomeRepository
class AddIncomeUseCase constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(income: Income) = repository.addIncome(income)
}
