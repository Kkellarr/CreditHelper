package com.example.credithelper.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credithelper.domain.usecase.CalculateDebtBurdenUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val calculateDebtBurdenUseCase: CalculateDebtBurdenUseCase
) : ViewModel() {

    private val _plannedLoan = MutableStateFlow(0.0)

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<DashboardEffect> = _effect.asSharedFlow()

    init {
        loadBurden()
    }

    fun dispatch(intent: DashboardIntent) {
        viewModelScope.launch {
            when (intent) {
                DashboardIntent.Load -> loadBurden()
                DashboardIntent.NavigateToIncomes -> _effect.emit(DashboardEffect.NavigateToIncomes)
                DashboardIntent.NavigateToDebts -> _effect.emit(DashboardEffect.NavigateToDebts)
                is DashboardIntent.UpdatePlannedLoan -> {
                    _plannedLoan.value = intent.amount
                    _state.update { it.copy(plannedLoanPayment = intent.amount) }
                }
            }
        }
    }

    private fun loadBurden() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            calculateDebtBurdenUseCase(_plannedLoan)
                .catch { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message ?: "Ошибка")
                    }
                }
                .collect { burden ->
                    _state.update {
                        it.copy(
                            debtBurden = burden,
                            plannedLoanPayment = _plannedLoan.value,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}
