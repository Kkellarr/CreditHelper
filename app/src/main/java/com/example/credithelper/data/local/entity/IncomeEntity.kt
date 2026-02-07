package com.example.credithelper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.credithelper.domain.model.Income

@Entity(tableName = "incomes")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amountMonthly: Double,
    val isConfirmed: Boolean
)

fun IncomeEntity.toDomain() = Income(
    id = id,
    name = name,
    amountMonthly = amountMonthly,
    isConfirmed = isConfirmed
)

fun Income.toEntity() = IncomeEntity(
    id = id,
    name = name,
    amountMonthly = amountMonthly,
    isConfirmed = isConfirmed
)
