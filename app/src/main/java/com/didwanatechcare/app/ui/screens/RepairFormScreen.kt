package com.didwanatechcare.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.didwanatechcare.app.ui.components.PhotoPickerRow
import com.didwanatechcare.app.util.Validation
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairFormScreen(onBack: () -> Unit, onSubmitted: (String) -> Unit) {
    val categories = listOf("Mobile Repair","Laptop Repair","Computer Repair","Printer Repair","CCTV / Camera","Other")
    var category by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var photos by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var error by remember { mutableStateOf("") }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { photos = it.take(5) }

    Scaffold(topBar = { TopAppBar(title = { Text("Repair Service") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            CategoryPicker("Category", categories, category) { category = it }
            LabeledField("Name", name, { name = it }, "Aapka naam")
            LabeledField("Mobile", mobile, { mobile = it }, "10 digit mobile")
            LabeledField("Address", address, { address = it }, "Poora address", maxLines = 3)
            LabeledField("Problem Description", problem, { problem = it }, "Problem detail me likhein", maxLines = 4)
            LabeledField("Notes (optional)", notes, { notes = it }, "Koi aur baat")
            LabeledField("Preferred Time (optional)", time, { time = it }, "e.g. Evening")
            PhotoPickerRow(photos.size, { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, { photos = emptyList() })
            if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(4.dp))
            Button(onClick = {
                val m = Validation.normalizeMobile(mobile)
                when {
                    category.isEmpty() -> error = "Category select karein"
                    name.trim().length < 3 -> error = "Naam kam se kam 3 akshar ka ho"
                    !Validation.isValidMobile(m) -> error = "Sahi 10-digit mobile number likhein"
                    address.trim().length < 10 -> error = "Poora address likhein"
                    problem.trim().length < 10 -> error = "Problem kam se kam 10 akshar me likhein"
                    else -> { error = ""; onSubmitted(UUID.randomUUID().toString()) }
                }
            }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) { Text("Submit Request") }
        }
    }
}