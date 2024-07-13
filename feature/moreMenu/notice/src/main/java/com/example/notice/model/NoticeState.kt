package com.example.notice.model

import com.mashup.core.network.dto.PushHistoryResponse

data class NoticeState(
    val newNoticeList: List<PushHistoryResponse.Notice> = emptyList(),
    val oldNoticeList: List<PushHistoryResponse.Notice> = emptyList()
)


