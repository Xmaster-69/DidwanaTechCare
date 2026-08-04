package com.didwanatechcare.app.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onBack: () -> Unit, onNavigateToAdminLogin: () -> Unit) {
    PlaceholderScreen("Settings", onBack = onBack, content = {
        Spacer(Modifier.height(16.dp))
        Button(onClick = onNavigateToAdminLogin) { Text("Admin Login") }
    })
}