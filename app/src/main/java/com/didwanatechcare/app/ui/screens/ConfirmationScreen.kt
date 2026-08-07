package com.didwanatechcare.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.didwanatechcare.app.util.SupportConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(requestId: String, onHome: () -> Unit) {
    val ctx = LocalContext.current
    Scaffold(topBar = { TopAppBar(title = { Text("Request Submitted") }) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Thank you!", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Hamari team jald hi aapse contact karegi.")
            Spacer(Modifier.height(16.dp))
            Text("Request ID: $requestId", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+${SupportConfig.CALL_NUMBER}")))
                }, modifier = Modifier.weight(1f)) { Text("Call Now") }
                Button(onClick = {
                    val msg = Uri.encode("Namaste! Mujhe Didwana TechCare se service chahiye. Request ID: $requestId")
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${SupportConfig.WA_NUMBER}?text=$msg")))
                }, modifier = Modifier.weight(1f)) { Text("WhatsApp") }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onHome, modifier = Modifier.fillMaxWidth()) { Text("Back to Home") }
        }
    }
}
