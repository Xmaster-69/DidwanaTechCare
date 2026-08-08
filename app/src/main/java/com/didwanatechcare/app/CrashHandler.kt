package com.didwanatechcare.app

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Global crash handler. Installed by [CrashProvider] (manifest init runs BEFORE
 * Application.onCreate and before Firebase's own ContentProviders), so even a
 * startup-time crash (Firebase init, class loading) is captured.
 *
 * Writes the report to THREE places so it can always be recovered:
 *  1. internal filesDir  -> shown as a dialog on next launch (MainActivity)
 *  2. external filesDir  -> /storage/emulated/0/Android/data/<pkg>/files/crash_report.txt
 *                           (copyable via USB / file manager, no root needed)
 *  3. logcat             -> `logcat -d | grep DidwanaTechCare`
 */
object CrashHandler {
    @Volatile private var app: Context? = null

    fun install(ctx: Context) {
        if (app != null) return
        app = ctx.applicationContext
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val now = System.currentTimeMillis()
            val report = buildString {
                appendLine("=== DIDWANA TECH CARE CRASH REPORT ===")
                appendLine("time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}")
                appendLine("device: ${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE}, API ${Build.VERSION.SDK_INT})")
                appendLine("thread: ${thread.name}")
                appendLine("exception: ${throwable.javaClass.name}: ${throwable.message}")
                throwable.stackTrace.take(30).forEach { appendLine("    at $it") }
                throwable.cause?.let { c ->
                    appendLine("caused by: ${c.javaClass.name}: ${c.message}")
                    c.stackTrace.take(15).forEach { appendLine("    at $it") }
                }
                appendLine("---")
            }
            Log.e("DidwanaTechCare", report)
            app?.let { a ->
                runCatching {
                    FileWriter(File(a.filesDir, "crash_report.txt"), true).use { w -> w.appendLine(report) }
                    val ext = a.getExternalFilesDir(null)
                    if (ext != null) {
                        FileWriter(File(ext, "crash_report.txt"), true).use { w -> w.appendLine(report) }
                    }
                }
            }
            // Crash-loop guard: if a crash happened within the last 5s, do NOT
            // auto-restart (prevents infinite restart loop on startup crashes).
            val prefs = ctx.getSharedPreferences("crash_guard", Context.MODE_PRIVATE)
            val last = prefs.getLong("last_crash_ms", 0L)
            prefs.edit().putLong("last_crash_ms", now).apply()
            if (now - last >= 5000L) {
                runCatching {
                    val i = Intent(ctx, MainActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    ctx.startActivity(i)
                }
            }
        }
    }
}