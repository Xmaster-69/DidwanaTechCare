package com.didwanatechcare.app.util

object Validation {
    fun normalizeMobile(raw: String): String {
        var d = raw.filter { it.isDigit() }
        if (d.length == 12 && d.startsWith("91")) d = d.takeLast(10)
        return d
    }
    fun isValidMobile(m: String) = m.matches(Regex("^[6-9][0-9]{9}$"))
}
