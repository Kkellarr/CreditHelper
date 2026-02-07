package com.example.credithelper.domain.repository

import com.example.credithelper.domain.model.Income
import kotlinx.coroutines.flow.Flow

interface IncomeRepository {
    fun getAllIncomes(): Flow<List<Income>>
    suspend fun addIncome(income: Income)
    suspend fun updateIncome(income: Income)
    suspend fun deleteIncome(id: Long)
    suspend fun getIncomeById(id: Long): Income?
}
