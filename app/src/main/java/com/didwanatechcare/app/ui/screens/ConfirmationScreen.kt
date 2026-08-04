package com.didwanatechcare.app.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationScreen(requestId: String, onHome: () -> Unit) {
    PlaceholderScreen("Request Submitted", "Thank you! Request ID: $requestId", content = {
        Spacer(Modifier.height(16.dp))
        Button(onClick = onHome) { Text("Back to Home") }
    })
}