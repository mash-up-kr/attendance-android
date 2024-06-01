package com.mashup.ui.schedule.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EventDetail(
    val id: Int,
    val type: EventDetailType,
    val header: Header? = null,
    val body: Body? = null,
    val location: Location? = null,
    val info: Info? = null
)

data class Header(
    val eventId: Int,
    val startedAt: Date,
    val endedAt: Date
) {
    fun getHeader() = "${eventId}부"
    fun getTimeStampStr(): String {
        return try {
            val timeLineFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH)
            "${timeLineFormat.format(startedAt)} - ${timeLineFormat.format(endedAt)}"
        } catch (ignore: Exception) {
            "??:?? - ??:??"
        }
    }
}

data class Body(
    val contentId: String,
    val title: String,
    val content: String,
    val startedAt: Date
) {
    fun getTimeStampStr(): String {
        return try {
            val timeLineFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH)
            timeLineFormat.format(startedAt)
        } catch (ignore: Exception) {
            "??:??"
        }
    }
}

data class Location(
    val detailAddress: String?,
    val roadAddress: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class Info(
    val title: String,
    val date: String,
    val time: String
)
