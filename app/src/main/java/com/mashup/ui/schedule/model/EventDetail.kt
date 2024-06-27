package com.mashup.ui.schedule.model

import com.mashup.core.common.extensions.getTimeFormat
import java.util.Date

sealed class EventDetail(
    open val index: Int,
    val type: EventDetailType
) {
    data class Header(
        override val index: Int,
        val eventId: Int,
        val startedAt: Date,
        val endedAt: Date
    ) : EventDetail(index, EventDetailType.HEADER) {
        val title = "${eventId}부"
        val formattedTime = "${startedAt.getTimeFormat()} - ${endedAt.getTimeFormat()}"
    }

    data class Content(
        override val index: Int,
        val contentId: String,
        val title: String,
        val content: String,
        val startedAt: Date
    ) : EventDetail(index, EventDetailType.CONTENT) {
        val formattedTime = startedAt.getTimeFormat()
    }

    data class Location(
        override val index: Int,
        val detailAddress: String,
        val roadAddress: String,
        val latitude: Double?,
        val longitude: Double?
    ) : EventDetail(index, EventDetailType.LOCATION)

    data class Info(
        override val index: Int,
        val title: String,
        val date: String,
        val startedAt: Date,
        val endedAt: Date
    ) : EventDetail(index, EventDetailType.INFO) {
        val formattedTime = "${startedAt.getTimeFormat()} - ${endedAt.getTimeFormat()}"
    }
}
