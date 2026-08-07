package com.didwanatechcare.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageCompressor {
    private const val MAX_DIM = 1280
    private const val JPEG_Q = 78
    private const val THUMB_DIM = 256
    private const val THUMB_Q = 70

    fun compressToFiles(ctx: Context, uri: Uri, prefix: String): Pair<File, File>? {
        val src = BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(uri) ?: return null) ?: return null
        val dir = File(ctx.cacheDir, "dtc_img").apply { if (!exists()) mkdirs() }
        val id = UUID.randomUUID().toString().take(8)
        val full = saveScaled(src, File(dir, "${prefix}_${id}_full.jpg"), MAX_DIM, JPEG_Q)
        val thumb = saveScaled(src, File(dir, "${prefix}_${id}_thumb.jpg"), THUMB_DIM, THUMB_Q)
        if (!src.isRecycled) src.recycle()
        return Pair(full, thumb)
    }

    private fun saveScaled(src: Bitmap, out: File, max: Int, q: Int): File {
        val w = src.width; val h = src.height
        val (nw, nh) = if (w > h && w > max) max to (h * max / w)
        else if (h > max) (w * max / h) to max
        else w to h
        val scaled = Bitmap.createScaledBitmap(src, nw, nh, true)
        FileOutputStream(out).use { fos ->
            val bos = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, q, bos)
            fos.write(bos.toByteArray())
        }
        if (scaled !== src && !scaled.isRecycled) scaled.recycle()
        return out
    }
}