package com.example.credithelper.domain.repository

import com.example.credithelper.domain.model.Debt
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllDebts(): Flow<List<Debt>>
    suspend fun addDebt(debt: Debt)
    suspend fun updateDebt(debt: Debt)
    suspend fun deleteDebt(id: Long)
    suspend fun getDebtById(id: Long): Debt?
}
