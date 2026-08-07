package com.didwanatechcare.app.data

import android.content.Context
import android.net.Uri
import com.didwanatechcare.app.util.CloudinaryUploader
import com.didwanatechcare.app.util.ImageCompressor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

data class RepairInput(val category: String, val name: String, val mobile: String, val address: String, val problem: String, val notes: String, val preferredTime: String, val photos: List<Uri>)
data class BuyInput(val category: String, val name: String, val mobile: String, val address: String, val notes: String)

sealed class SubmitResult {
    data class Success(val requestId: String) : SubmitResult()
    data class Error(val msg: String) : SubmitResult()
}

object SubmissionRepository {
    private val auth = FirebaseAuth.getInstance()
    private val fs = FirebaseFirestore.getInstance()

    private suspend fun ensureAnon(): String {
        val cur = auth.currentUser
        if (cur != null && cur.isAnonymous) return cur.uid
        return auth.signInAnonymously().await().user?.uid ?: throw IllegalStateException("Anonymous auth failed")
    }

    private suspend fun uploadPair(ctx: Context, uri: Uri, prefix: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        val (full, thumb) = ImageCompressor.compressToFiles(ctx, uri, prefix) ?: return@withContext null
        val pid = UUID.randomUUID().toString().take(12)
        val fullUrl = CloudinaryUploader.upload(full, "dtc/${pid}_full") ?: run { cleanup(full, thumb); return@withContext null }
        val thumbUrl = CloudinaryUploader.upload(thumb, "dtc/${pid}_thumb") ?: run { cleanup(full, thumb); return@withContext null }
        cleanup(full, thumb)
        Pair(fullUrl, thumbUrl)
    }

    private fun cleanup(vararg files: File) { files.forEach { runCatching { it.delete() } } }

    suspend fun submitRepair(ctx: Context, input: RepairInput): SubmitResult {
        return try {
            val uid = ensureAnon()
            val requestId = UUID.randomUUID().toString()
            val fulls = mutableListOf<String>(); val thumbs = mutableListOf<String>()
            input.photos.forEachIndexed { i, uri ->
                uploadPair(ctx, uri, "r$i")?.let { (f, t) -> fulls += f; thumbs += t }
            }
            val doc = mapOf(
                "id" to requestId, "type" to "repair", "status" to "new",
                "category" to input.category, "problemDesc" to input.problem,
                "notes" to input.notes, "preferredTime" to input.preferredTime,
                "name" to input.name, "nameLower" to input.name.lowercase(),
                "mobile" to input.mobile, "address" to input.address,
                "customerUid" to uid,
                "photos" to fulls, "thumbnails" to thumbs,
                "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp(),
                "contactedAt" to null, "completedAt" to null, "cancelledAt" to null,
                "adminNotes" to "", "assignedWorker" to "",
                "appVersion" to "1.0.0", "platform" to "android"
            )
            fs.collection("requests").document(requestId).set(doc).await()
            SubmitResult.Success(requestId)
        } catch (e: Exception) {
            SubmitResult.Error(e.message ?: "Submit failed")
        }
    }

    suspend fun submitBuy(ctx: Context, input: BuyInput): SubmitResult {
        return try {
            val uid = ensureAnon()
            val requestId = UUID.randomUUID().toString()
            val doc = mapOf(
                "id" to requestId, "type" to "buy", "status" to "new",
                "category" to input.category, "problemDesc" to "",
                "notes" to input.notes, "preferredTime" to "",
                "name" to input.name, "nameLower" to input.name.lowercase(),
                "mobile" to input.mobile, "address" to input.address,
                "customerUid" to uid,
                "photos" to emptyList<String>(), "thumbnails" to emptyList<String>(),
                "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp(),
                "contactedAt" to null, "completedAt" to null, "cancelledAt" to null,
                "adminNotes" to "", "assignedWorker" to "",
                "appVersion" to "1.0.0", "platform" to "android"
            )
            fs.collection("requests").document(requestId).set(doc).await()
            SubmitResult.Success(requestId)
        } catch (e: Exception) {
            SubmitResult.Error(e.message ?: "Submit failed")
        }
    }
}