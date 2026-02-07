package com.example.credithelper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.credithelper.domain.model.Debt
import com.example.credithelper.domain.model.DebtType

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val monthlyPayment: Double,
    val totalAmount: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val type: DebtType
)

fun DebtEntity.toDomain() = Debt(
    id = id,
    name = name,
    monthlyPayment = monthlyPayment,
    totalAmount = totalAmount,
    remainingAmount = remainingAmount,
    type = type
)

fun Debt.toEntity() = DebtEntity(
    id = id,
    name = name,
    monthlyPayment = monthlyPayment,
    totalAmount = totalAmount,
    remainingAmount = remainingAmount,
    type = type
)
