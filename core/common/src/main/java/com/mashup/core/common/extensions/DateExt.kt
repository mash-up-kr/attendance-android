package com.mashup.core.common.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.format(pattern: String, locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat(pattern, locale)
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
    return dateFormat.format(this)
}

fun Date.getTimeFormat(): String {
    return try {
        val timeLineFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH)
        timeLineFormat.format(this)
    } catch (ignore: Exception) {
        "??:??"
    }
}

fun Date.year(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

fun Date.month(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH) + 1
}

fun Date.day(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DATE)
}

fun Date.week(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "월"
        Calendar.TUESDAY -> "화"
        Calendar.WEDNESDAY -> "수"
        Calendar.THURSDAY -> "목"
        Calendar.FRIDAY -> "금"
        Calendar.SATURDAY -> "토"
        Calendar.SUNDAY -> "일"
        else -> ""
    }
}
