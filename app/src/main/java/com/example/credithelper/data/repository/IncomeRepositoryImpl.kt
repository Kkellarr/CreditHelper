package com.example.credithelper.data.repository

import com.example.credithelper.data.local.dao.IncomeDao
import com.example.credithelper.data.local.entity.toDomain
import com.example.credithelper.data.local.entity.toEntity
import com.example.credithelper.domain.model.Income
import com.example.credithelper.domain.repository.IncomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IncomeRepositoryImpl(
    private val dao: IncomeDao
) : IncomeRepository {
    override fun getAllIncomes(): Flow<List<Income>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addIncome(income: Income) {
        val entity = income.toEntity().copy(id = if (income.id == 0L) 0 else income.id)
        dao.insert(entity)
    }

    override suspend fun updateIncome(income: Income) {
        dao.update(income.toEntity())
    }

    override suspend fun deleteIncome(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun getIncomeById(id: Long): Income? =
        dao.getById(id)?.toDomain()
}
