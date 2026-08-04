package com.didwanatechcare.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.didwanatechcare.app.ui.screens.BuyFormScreen
import com.didwanatechcare.app.ui.screens.ConfirmationScreen
import com.didwanatechcare.app.ui.screens.HomeScreen
import com.didwanatechcare.app.ui.screens.RepairFormScreen
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
        composable(Routes.REPAIR_FORM) { RepairFormScreen { nav.popBackStack() } }
        composable(Routes.BUY_FORM) { BuyFormScreen { nav.popBackStack() } }
        composable(Routes.CONFIRMATION) { e ->
            ConfirmationScreen(e.arguments?.getString("requestId") ?: "") {
                nav.popBackStack(Routes.HOME, false)
            }
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { nav.popBackStack() }, onNavigateToAdminLogin = { nav.navigate(Routes.ADMIN_LOGIN) })
        }
    }
}