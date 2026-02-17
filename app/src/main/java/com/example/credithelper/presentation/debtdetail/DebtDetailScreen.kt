package com.example.credithelper.presentation.debtdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.credithelper.domain.model.DebtType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtDetailScreen(
    viewModel: DebtDetailViewModel,
    onBack: () -> Unit,
    onNavigateToRepay: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.dispatch(DebtDetailIntent.Load)
    }

    LaunchedEffect(state.saved) {
        if (state.saved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.debt?.name ?: "Редактирование кредита") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading && state.debt == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = { viewModel.dispatch(DebtDetailIntent.UpdateName(it)) },
                            label = { Text("Название") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = state.amountStr,
                            onValueChange = {
                                val filtered = it.filter { c -> c.isDigit() || c == '.' }
                                viewModel.dispatch(DebtDetailIntent.UpdateAmount(filtered))
                            },
                            label = { Text("Платёж в месяц (₽)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = state.totalAmountStr,
                            onValueChange = {
                                val filtered = it.filter { c -> c.isDigit() || c == '.' }
                                viewModel.dispatch(DebtDetailIntent.UpdateTotalAmount(filtered))
                            },
                            label = { Text("Тело кредита (₽)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = state.remainingAmountStr,
                            onValueChange = {
                                val filtered = it.filter { c -> c.isDigit() || c == '.' }
                                viewModel.dispatch(DebtDetailIntent.UpdateRemainingAmount(filtered))
                            },
                            label = { Text("Остаток к выплате (₽)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Тип:", modifier = Modifier.padding(top = 16.dp))
                            FilterChip(
                                selected = state.type == DebtType.LOAN,
                                onClick = { viewModel.dispatch(DebtDetailIntent.UpdateType(DebtType.LOAN)) },
                                label = { Text("Кредит") }
                            )
                            FilterChip(
                                selected = state.type == DebtType.CREDIT_CARD,
                                onClick = { viewModel.dispatch(DebtDetailIntent.UpdateType(DebtType.CREDIT_CARD)) },
                                label = { Text("Кредитная карта") }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = onNavigateToRepay,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Погасить кредит")
                }

                Spacer(Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = { viewModel.dispatch(DebtDetailIntent.Save) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Сохранить")
                }

                AnimatedVisibility(
                    visible = state.error != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    state.error?.let { err ->
                        Text(
                            text = err,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
