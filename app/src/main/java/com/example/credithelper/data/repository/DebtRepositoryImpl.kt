package com.example.credithelper.data.repository

import com.example.credithelper.data.local.dao.DebtDao
import com.example.credithelper.data.local.entity.toDomain
import com.example.credithelper.data.local.entity.toEntity
import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DebtRepositoryImpl(
    private val dao: DebtDao
) : DebtRepository {
    override fun getAllDebts(): Flow<List<Debt>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addDebt(debt: Debt) {
        val entity = debt.toEntity().copy(id = if (debt.id == 0L) 0 else debt.id)
        dao.insert(entity)
    }

    override suspend fun updateDebt(debt: Debt) {
        dao.update(debt.toEntity())
    }

    override suspend fun deleteDebt(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun getDebtById(id: Long): Debt? =
        dao.getById(id)?.toDomain()
}
