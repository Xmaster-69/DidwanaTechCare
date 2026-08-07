package com.didwanatechcare.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledField(label: String, value: String, onValueChange: (String) -> Unit, hint: String = "", maxLines: Int = 1) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = value, onValueChange = onValueChange, placeholder = { Text(hint) },
            modifier = Modifier.fillMaxWidth(), maxLines = maxLines, singleLine = maxLines == 1
        )
    }
}

@Composable
fun CategoryPicker(label: String, categories: List<String>, selected: String, onSelect: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(onClick = { open = true }, modifier = Modifier.fillMaxWidth()) {
            Text(if (selected.isEmpty()) "Select" else selected)
        }
        if (open) AlertDialog(
            onDismissRequest = { open = false },
            confirmButton = { TextButton(onClick = { open = false }) { Text("Cancel") } },
            title = { Text(label) },
            text = {
                Column { categories.forEach { c ->
                    TextButton(onClick = { onSelect(c); open = false }, modifier = Modifier.fillMaxWidth()) { Text(c) }
                }}
            }
        )
    }
}

@Composable
fun PhotoPickerRow(count: Int, onPick: () -> Unit, onClear: () -> Unit) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text("Photos (optional, max 5)", style = MaterialTheme.typography.bodyMedium)
        Row {
            OutlinedButton(onClick = onPick) { Text("Add Photos ($count/5)") }
            if (count > 0) TextButton(onClick = onClear) { Text("Remove All") }
        }
    }
}