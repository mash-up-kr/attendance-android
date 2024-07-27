package com.mashup.core.network.dto

data class PushHistoryResponse(
    val read: List<Notice>,
    val unread: List<Notice>
) {
    data class Notice(
        val pushType: String,
        val title: String,
        val body: String,
        val linkType: String,
        val sendTime: String
    )
}
