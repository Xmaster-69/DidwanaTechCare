package com.didwanatechcare.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRepair: () -> Unit,
    onNavigateToBuy: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Didwana TechCare") }, actions = {
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        })
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Home (skeleton)", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(24.dp))
            Button(onClick = onNavigateToRepair, modifier = Modifier.fillMaxWidth()) { Text("Repair Service") }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onNavigateToBuy, modifier = Modifier.fillMaxWidth()) { Text("Buy Product") }
        }
    }
}