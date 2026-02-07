package com.example.credithelper.di

import android.content.Context
import com.example.credithelper.data.local.AppDatabase
import com.example.credithelper.data.repository.DebtRepositoryImpl
import com.example.credithelper.data.repository.IncomeRepositoryImpl
import com.example.credithelper.domain.repository.DebtRepository
import com.example.credithelper.domain.repository.IncomeRepository
import com.example.credithelper.domain.usecase.CalculateDebtBurdenUseCase
import com.example.credithelper.domain.usecase.debt.AddDebtUseCase
import com.example.credithelper.domain.usecase.debt.DeleteDebtUseCase
import com.example.credithelper.domain.usecase.debt.GetDebtsUseCase
import com.example.credithelper.domain.usecase.debt.GetDebtByIdUseCase
import com.example.credithelper.domain.usecase.debt.UpdateDebtUseCase
import com.example.credithelper.domain.usecase.income.AddIncomeUseCase
import com.example.credithelper.domain.usecase.income.DeleteIncomeUseCase
import com.example.credithelper.domain.usecase.income.GetIncomeByIdUseCase
import com.example.credithelper.domain.usecase.income.GetIncomesUseCase
import com.example.credithelper.domain.usecase.income.UpdateIncomeUseCase
import com.example.credithelper.presentation.dashboard.DashboardViewModel
import com.example.credithelper.presentation.debtdetail.DebtDetailViewModel
import com.example.credithelper.presentation.debts.DebtsViewModel
import com.example.credithelper.presentation.incomedetail.IncomeDetailViewModel
import com.example.credithelper.presentation.incomes.IncomesViewModel

object AppModule {
    private var db: AppDatabase? = null

    fun init(context: Context) {
        db = AppDatabase.create(context)
    }

    private fun getDb(): AppDatabase = db!!

    private val incomeRepository: IncomeRepository by lazy {
        IncomeRepositoryImpl(getDb().incomeDao())
    }

    private val debtRepository: DebtRepository by lazy {
        DebtRepositoryImpl(getDb().debtDao())
    }

    fun getDashboardViewModel(): DashboardViewModel =
        DashboardViewModel(
            calculateDebtBurdenUseCase = CalculateDebtBurdenUseCase(
                incomeRepository,
                debtRepository
            )
        )

    fun getIncomesViewModel(): IncomesViewModel =
        IncomesViewModel(
            getIncomesUseCase = GetIncomesUseCase(incomeRepository),
            addIncomeUseCase = AddIncomeUseCase(incomeRepository),
            deleteIncomeUseCase = DeleteIncomeUseCase(incomeRepository)
        )

    fun getDebtsViewModel(): DebtsViewModel =
        DebtsViewModel(
            getDebtsUseCase = GetDebtsUseCase(debtRepository),
            addDebtUseCase = AddDebtUseCase(debtRepository),
            deleteDebtUseCase = DeleteDebtUseCase(debtRepository)
        )

    fun createDebtDetailViewModel(debtId: Long): DebtDetailViewModel =
        DebtDetailViewModel(
            debtId = debtId,
            getDebtByIdUseCase = GetDebtByIdUseCase(debtRepository),
            updateDebtUseCase = UpdateDebtUseCase(debtRepository)
        )

    fun createIncomeDetailViewModel(incomeId: Long): IncomeDetailViewModel =
        IncomeDetailViewModel(
            incomeId = incomeId,
            getIncomeByIdUseCase = GetIncomeByIdUseCase(incomeRepository),
            updateIncomeUseCase = UpdateIncomeUseCase(incomeRepository)
        )
}
