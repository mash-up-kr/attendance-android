package com.mashup.core.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushHistoryResponse(
    val read: List<Notice>,
    val unread: List<Notice>
) {
    @JsonClass(generateAdapter = true)
    data class Notice(
        val pushType: String,
        val title: String,
        val body: String,
        val linkType: String,
        val sendTime: String
    )
}
