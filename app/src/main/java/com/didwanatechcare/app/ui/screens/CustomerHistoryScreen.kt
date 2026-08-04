package com.didwanatechcare.app.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun CustomerHistoryScreen(mobile: String, onBack: () -> Unit) {
    PlaceholderScreen("Customer History", "Mobile: $mobile – Phase 6", onBack)
}