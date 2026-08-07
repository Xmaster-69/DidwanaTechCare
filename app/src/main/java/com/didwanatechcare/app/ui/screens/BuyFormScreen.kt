package com.didwanatechcare.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.didwanatechcare.app.ui.components.CategoryPicker
import com.didwanatechcare.app.ui.components.LabeledField
import com.didwanatechcare.app.util.Validation
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyFormScreen(onBack: () -> Unit, onSubmitted: (String) -> Unit) {
    val categories = listOf("Mobile","Laptop","Computer","Printer","CCTV / Camera","Accessory","Other")
    var category by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Buy Product") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            CategoryPicker("Category", categories, category) { category = it }
            LabeledField("Name", name, { name = it }, "Aapka naam")
            LabeledField("Mobile", mobile, { mobile = it }, "10 digit mobile")
            LabeledField("Address (optional)", address, { address = it }, "Address", maxLines = 3)
            LabeledField("Notes (optional)", notes, { notes = it }, "Kaunsa product chahiye")
            if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(4.dp))
            Button(onClick = {
                val m = Validation.normalizeMobile(mobile)
                when {
                    category.isEmpty() -> error = "Category select karein"
                    name.trim().length < 3 -> error = "Naam kam se kam 3 akshar ka ho"
                    !Validation.isValidMobile(m) -> error = "Sahi 10-digit mobile number likhein"
                    else -> { error = ""; onSubmitted(UUID.randomUUID().toString()) }
                }
            }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Submit Enquiry") }
        }
    }
}