package com.didwanatechcare.app.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun RequestDetailScreen(requestId: String, onBack: () -> Unit) {
    PlaceholderScreen("Request Detail", "ID: $requestId – Phase 6", onBack)
}