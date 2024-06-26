package com.mashup.ui.schedule.model

import com.mashup.data.dto.ContentResponse
import com.mashup.data.dto.EventResponse
import com.mashup.data.dto.ScheduleResponse
import java.util.Date
import javax.inject.Inject

class EventDetailMapper @Inject constructor() {
    fun getEventDetailList(
        title: String,
        date: String,
        eventList: List<EventResponse>,
        location: ScheduleResponse.Location?
    ): List<EventDetail> {
        val eventDetailList = mutableListOf<EventDetail>()
        var index = 0

        val infoModel = mapToInfoModel(index++, title, date, eventList.first().startedAt, eventList.last().endedAt)
        eventDetailList.add(infoModel)

        if (location?.detailAddress != null) { // 위치 정보가 있는 경우(온라인이면 placeName이 Zoom으로 내려옴)
            val locationModel = mapToLocationModel(index++, location)
            eventDetailList.add(locationModel)
        }

        eventList.forEachIndexed { eventIndex, event ->
            val headerModel = mapToHeaderModel(index++, eventIndex, event)
            eventDetailList.add(headerModel)

            event.contentList.forEachIndexed { contentIndex, content ->
                val contentModel = mapToContentModel(index++, contentIndex, content)
                eventDetailList.add(contentModel)
            }
        }

        return eventDetailList
    }

    private fun mapToHeaderModel(index: Int, eventIndex: Int, event: EventResponse): EventDetail {
        return EventDetail.Header(
            index = index,
            eventId = eventIndex + 1,
            startedAt = event.startedAt,
            endedAt = event.endedAt,
        )
    }

    private fun mapToContentModel(
        index: Int,
        contentIndex: Int,
        content: ContentResponse
    ): EventDetail {
        return EventDetail.Content(
            index = index,
            contentId = "${contentIndex + 1}",
            title = content.title,
            content = content.content.orEmpty(),
            startedAt = content.startedAt,
        )
    }

    private fun mapToLocationModel(index: Int, location: ScheduleResponse.Location): EventDetail {
        return EventDetail.Location(
            index = index,
            detailAddress = location.detailAddress.orEmpty(),
            roadAddress = location.roadAddress.orEmpty(),
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private fun mapToInfoModel(
        index: Int,
        title: String,
        date: String,
        startedAt: Date,
        endedAt: Date,
    ): EventDetail {
        return EventDetail.Info(
            index = index,
            title = title,
            date = date,
            startedAt = startedAt,
            endedAt = endedAt,
        )
    }
}
