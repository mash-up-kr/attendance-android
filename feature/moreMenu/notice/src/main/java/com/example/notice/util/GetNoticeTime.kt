package com.example.notice.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun String.getNoticeTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val parsedTime = formatter.parse(this) ?: return ""
    val currentTime = Date()
    val diff = currentTime.time - parsedTime.time

    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

    return when {
        hours < 1 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 ${minutes}분 전"
        else -> {
            val monthDayFormatter = SimpleDateFormat("MM월 dd일", Locale.getDefault())
            monthDayFormatter.format(parsedTime)
        }
    }
}
