package com.example.credithelper.domain.usecase.income

import com.example.credithelper.domain.model.Income
import com.example.credithelper.domain.repository.IncomeRepository

class GetIncomeByIdUseCase constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(id: Long): Income? = repository.getIncomeById(id)
}
