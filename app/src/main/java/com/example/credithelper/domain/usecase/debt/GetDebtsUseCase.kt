package com.example.credithelper.domain.usecase.debt

import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
class GetDebtsUseCase constructor(
    private val repository: DebtRepository
) {
    operator fun invoke(): Flow<List<Debt>> = repository.getAllDebts()
}
