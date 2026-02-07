package com.example.credithelper.domain.usecase.income

import com.example.credithelper.domain.model.Income
import com.example.credithelper.domain.repository.IncomeRepository
import kotlinx.coroutines.flow.Flow
class GetIncomesUseCase constructor(
    private val repository: IncomeRepository
) {
    operator fun invoke(): Flow<List<Income>> = repository.getAllIncomes()
}
