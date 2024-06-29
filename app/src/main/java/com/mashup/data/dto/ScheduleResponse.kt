package com.mashup.data.dto

import android.annotation.SuppressLint
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@JsonClass(generateAdapter = true)
data class ScheduleResponse(
    @field:Json(name="scheduleType")
    val scheduleType: String,
    @field:Json(name = "scheduleId")
    val scheduleId: Int,
    @field:Json(name = "dateCount")
    val dateCount: Int,
    @field:Json(name = "generationNumber")
    val generationNumber: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "eventList")
    val eventList: List<EventResponse>,
    @field:Json(name = "startedAt")
    val startedAt: Date,
    @field:Json(name = "endedAt")
    val endedAt: Date,
    @field:Json(name = "location")
    val location: Location?
) {

    @JsonClass(generateAdapter = true)
    data class Location(
        @field:Json(name = "latitude")
        val latitude: Double?,
        @field:Json(name = "longitude")
        val longitude: Double?,
        @field:Json(name = "roadAddress")
        val roadAddress: String?,
        @field:Json(name = "detailAddress")
        val detailAddress: String?
    )

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        return try {
            val dateFormat = SimpleDateFormat("MM월 dd일")
            return dateFormat.format(startedAt)
        } catch (ignore: Exception) {
            "오류로 인해 알 수 없음"
        }
    }

    fun getDDay(): String {
        return when {
            dateCount == 0 -> {
                "D-Day"
            }
            dateCount > 0 -> {
                "D-$dateCount"
            }
            else -> {
                "D+${-dateCount}"
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeLine(): String {
        return try {
            val timeLineFormat = SimpleDateFormat("a hh:mm", Locale.KOREA)
            "${timeLineFormat.format(startedAt)} - ${timeLineFormat.format(endedAt)}"
        } catch (ignore: Exception) {
            "오류로 인해 알 수 없음"
        }
    }
}
