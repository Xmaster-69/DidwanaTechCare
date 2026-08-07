package com.didwanatechcare.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
        return try {
            val sampled = decodeSampled(ctx, uri) ?: return null
            val src = applyExifRotation(ctx, uri, sampled)
            val dir = File(ctx.cacheDir, "dtc_img").apply { if (!exists()) mkdirs() }
            val id = UUID.randomUUID().toString().take(8)
            val full = saveScaled(src, File(dir, "${prefix}_${id}_full.jpg"), MAX_DIM, JPEG_Q)
            val thumb = saveScaled(src, File(dir, "${prefix}_${id}_thumb.jpg"), THUMB_DIM, THUMB_Q)
            if (!src.isRecycled) src.recycle()
            Pair(full, thumb)
        } catch (e: OutOfMemoryError) {
            null
        } catch (e: Exception) {
            null
        }
    }

    /** Bounds-checked sampled decode — prevents OOM on high-res (e.g. 50MP) photos. */
    private fun decodeSampled(ctx: Context, uri: Uri): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        ctx.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) } ?: return null
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
        var sample = 1
        while (bounds.outWidth / (sample * 2) >= MAX_DIM && bounds.outHeight / (sample * 2) >= MAX_DIM) sample *= 2
        val opts = BitmapFactory.Options().apply { inSampleSize = sample }
        return ctx.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opts) }
    }

    /** Apply EXIF orientation so portrait photos are not stored rotated. */
    private fun applyExifRotation(ctx: Context, uri: Uri, src: Bitmap): Bitmap {
        val deg = runCatching {
            val exif = ctx.contentResolver.openInputStream(uri)?.use { ExifInterface(it) }
            when (exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        }.getOrDefault(0f)
        if (deg == 0f) return src
        val m = Matrix().apply { postRotate(deg) }
        val rotated = Bitmap.createBitmap(src, 0, 0, src.width, src.height, m, true)
        if (rotated !== src && !src.isRecycled) src.recycle()
        return rotated
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
