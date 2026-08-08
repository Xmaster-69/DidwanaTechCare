package com.didwanatechcare.app

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * Installs the global crash handler at the EARLIEST possible point in app
 * startup. ContentProviders are created before Application.onCreate; with
 * android:initOrder="100000000" this one runs BEFORE Firebase's own providers
 * (FirebaseInitProvider, AppCheck, etc.), so even a crash during Firebase
 * init is captured and reported to the next launch.
 */
class CrashProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.let { CrashHandler.install(it) }
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}