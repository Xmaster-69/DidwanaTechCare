package com.didwanatechcare.app.util

import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object CloudinaryUploader {
    private const val CLOUD = "rdj5llvu"
    private const val PRESET = "dtc_uploads"

    fun upload(file: File, publicId: String): String? {
        val url = URL("https://api.cloudinary.com/v1_1/$CLOUD/image/upload")
        val boundary = "----DTCCloudinary${System.currentTimeMillis()}"
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"; doOutput = true; useCaches = false
            setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
        }
        DataOutputStream(conn.outputStream).use { out ->
            val crlf = "\r\n"; val dd = "--"
            out.writeBytes("$dd$boundary$crlf"); out.writeBytes("Content-Disposition: form-data; name=\"upload_preset\"$crlf$crlf"); out.write(PRESET.toByteArray()); out.writeBytes(crlf)
            out.writeBytes("$dd$boundary$crlf"); out.writeBytes("Content-Disposition: form-data; name=\"public_id\"$crlf$crlf"); out.write(publicId.toByteArray()); out.writeBytes(crlf)
            out.writeBytes("$dd$boundary$crlf"); out.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"${file.name}\"$crlf"); out.writeBytes("Content-Type: image/jpeg$crlf$crlf")
            file.inputStream().use { it.copyTo(out) }
            out.writeBytes("$crlf$dd$boundary$dd$crlf")
        }
        val code = conn.responseCode
        val body = if (code in 200..299) conn.inputStream.bufferedReader().readText() else conn.errorStream?.bufferedReader()?.readText() ?: ""
        conn.disconnect()
        if (code !in 200..299) return null
        return JSONObject(body).getString("secure_url")
    }
}