package com.example.credithelper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.credithelper.di.AppModule
import com.example.credithelper.presentation.dashboard.DashboardScreen
import com.example.credithelper.presentation.debtdetail.DebtDetailScreen
import com.example.credithelper.presentation.repaydebt.RepayDebtScreen
import com.example.credithelper.presentation.debts.DebtsScreen
import com.example.credithelper.presentation.incomedetail.IncomeDetailScreen
import com.example.credithelper.presentation.incomes.IncomesScreen

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Incomes : Screen("incomes")
    data object Debts : Screen("debts")
    data object DebtDetail : Screen("debts/{debtId}") {
        fun createRoute(debtId: Long) = "debts/$debtId"
    }
    data object RepayDebt : Screen("debts/{debtId}/repay") {
        fun createRoute(debtId: Long) = "debts/$debtId/repay"
    }
    data object IncomeDetail : Screen("incomes/{incomeId}") {
        fun createRoute(incomeId: Long) = "incomes/$incomeId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            val viewModel = remember { AppModule.getDashboardViewModel() }
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToIncomes = { navController.navigate(Screen.Incomes.route) },
                onNavigateToDebts = { navController.navigate(Screen.Debts.route) }
            )
        }
        composable(Screen.Incomes.route) {
            val viewModel = remember { AppModule.getIncomesViewModel() }
            IncomesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onIncomeClick = { incomeId -> navController.navigate(Screen.IncomeDetail.createRoute(incomeId)) }
            )
        }
        composable(
            route = Screen.IncomeDetail.route,
            arguments = listOf(navArgument("incomeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val incomeId = backStackEntry.arguments?.getLong("incomeId") ?: 0L
            val viewModel: com.example.credithelper.presentation.incomedetail.IncomeDetailViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        AppModule.createIncomeDetailViewModel(incomeId) as T
                }
            )
            IncomeDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Debts.route) {
            val viewModel = remember { AppModule.getDebtsViewModel() }
            DebtsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onDebtClick = { debtId -> navController.navigate(Screen.DebtDetail.createRoute(debtId)) }
            )
        }
        composable(
            route = Screen.DebtDetail.route,
            arguments = listOf(navArgument("debtId") { type = NavType.LongType })
        ) { backStackEntry ->
            val debtId = backStackEntry.arguments?.getLong("debtId") ?: 0L
            val viewModel: com.example.credithelper.presentation.debtdetail.DebtDetailViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        AppModule.createDebtDetailViewModel(debtId) as T
                }
            )
            DebtDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRepay = { navController.navigate(Screen.RepayDebt.createRoute(debtId)) }
            )
        }
        composable(
            route = Screen.RepayDebt.route,
            arguments = listOf(navArgument("debtId") { type = NavType.LongType })
        ) { backStackEntry ->
            val repayDebtId = backStackEntry.arguments?.getLong("debtId") ?: 0L
            val repayViewModel: com.example.credithelper.presentation.repaydebt.RepayDebtViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        AppModule.createRepayDebtViewModel(repayDebtId) as T
                }
            )
            RepayDebtScreen(
                viewModel = repayViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
