package com.didwanatechcare.app

import android.app.Application
import android.content.Intent
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val now = System.currentTimeMillis()
            val prefs = getSharedPreferences("crash_guard", MODE_PRIVATE)
            val last = prefs.getLong("last_crash_ms", 0L)
            prefs.edit().putLong("last_crash_ms", now).apply()
            // Crash-loop guard: if a crash happened within the last 5s, do NOT
            // auto-restart (prevents infinite restart loop on startup crashes).
            val inLoop = (now - last) < 5000L
            val report = buildString {
                appendLine("=== DIDWANA TECH CARE CRASH REPORT ===")
                appendLine("time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}")
                appendLine("device: ${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE}, API ${Build.VERSION.SDK_INT})")
                appendLine("thread: ${thread.name}")
                appendLine("exception: ${throwable.javaClass.name}: ${throwable.message}")
                throwable.stackTrace.take(30).forEach { appendLine("    at $it") }
            }
            Log.e("DidwanaTechCare", report)
            runCatching {
                FileWriter(File(filesDir, "crash_report.txt"), true).use { w ->
                    w.appendLine(report)
                    w.appendLine("---")
                }
            }
            // Restart cleanly so the user lands on a working screen; the crash dialog
            // on next launch surfaces the report (see MainActivity).
            if (!inLoop) {
                runCatching {
                    val i = Intent(this, MainActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(i)
                }
            }
        }
    }
}
