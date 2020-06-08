package com.egiwon.scopedstorageexample.ext

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

fun Long.toFileSizeUnit(): String {
    if (this <= 0) return "0"
    val units: Array<String> = arrayOf("B", "KB", "MB", "GB")
    val digitGroup = (log10(this.toDouble()) / log10(1024.toDouble())).toInt()
    return "${getDecimalFormat(this, digitGroup)} ${units[digitGroup]}"
}

private fun getDecimalFormat(size: Long, digitGroup: Int) =
    DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroup.toDouble()))

fun Long.toLastModifiedTime(): String {
    val date = Date(this)
    return SimpleDateFormat("YY/MM/dd", Locale.getDefault()).format(date)
}