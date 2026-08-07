package com.didwanatechcare.app

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.didwanatechcare.app.navigation.AppNavigation
import com.didwanatechcare.app.ui.theme.DidwanaTechCareTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DidwanaTechCareTheme {
                Surface(modifier = Modifier.fillMaxSize()) { AppNavigation() }
            }
        }
        showPendingCrashReport()
    }

    /** Surface the previous crash (written by [App]) so it can be reported. */
    private fun showPendingCrashReport() {
        val file = File(filesDir, "crash_report.txt")
        if (!file.exists()) return
        val text = file.readText()
        if (text.isBlank()) { file.delete(); return }
        file.delete()
        val first = text.lineSequence().firstOrNull { it.startsWith("exception:") } ?: text.take(200)
        AlertDialog.Builder(this)
            .setTitle("App crash hua tha")
            .setMessage("Pichle launch par crash hua. Detail:\n\n$first\n\nPoora report: app ka internal storage > crash_report.txt")
            .setPositiveButton("OK") { d, _ -> d.dismiss() }
            .show()
    }
}