package com.didwanatechcare.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.didwanatechcare.app.ui.screens.AdminDashboardScreen
import com.didwanatechcare.app.ui.screens.AdminLoginScreen
import com.didwanatechcare.app.ui.screens.BuyFormScreen
import com.didwanatechcare.app.ui.screens.ConfirmationScreen
import com.didwanatechcare.app.ui.screens.CustomerHistoryScreen
import com.didwanatechcare.app.ui.screens.HomeScreen
import com.didwanatechcare.app.ui.screens.RepairFormScreen
import com.didwanatechcare.app.ui.screens.RequestDetailScreen
import com.didwanatechcare.app.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToRepair = { nav.navigate(Routes.REPAIR_FORM) },
                onNavigateToBuy = { nav.navigate(Routes.BUY_FORM) },
                onNavigateToSettings = { nav.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.REPAIR_FORM) {
            RepairFormScreen(onBack = { nav.popBackStack() }, onSubmitted = { id -> nav.navigate(Routes.confirmation(id)) })
        }
        composable(Routes.BUY_FORM) {
            BuyFormScreen(onBack = { nav.popBackStack() }, onSubmitted = { id -> nav.navigate(Routes.confirmation(id)) })
        }
        composable(Routes.CONFIRMATION) { e ->
            ConfirmationScreen(e.arguments?.getString("requestId") ?: "") { nav.popBackStack(Routes.HOME, false) }
        }
        composable(Routes.ADMIN_LOGIN) { AdminLoginScreen { nav.popBackStack() } }
        composable(Routes.ADMIN_DASHBOARD) { AdminDashboardScreen() }
        composable(Routes.REQUEST_DETAIL) { e ->
            RequestDetailScreen(e.arguments?.getString("requestId") ?: "") { nav.popBackStack() }
        }
        composable(Routes.CUSTOMER_HISTORY) { e ->
            CustomerHistoryScreen(e.arguments?.getString("mobile") ?: "") { nav.popBackStack() }
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { nav.popBackStack() }, onNavigateToAdminLogin = { nav.navigate(Routes.ADMIN_LOGIN) })
        }
    }
}