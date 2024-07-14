package com.example.notice.util

import com.mashup.core.network.dto.PushHistoryResponse

fun PushHistoryResponse.Notice.getNoticeTitle(): String {
    return when (pushType) {
        "BIRTHDAY" -> {
            "생일 축하"
        }

        "MASHONG" -> {
            "매숑이 키우기"
        }

        "DANGGN" -> {
            "당근 흔들기"
        }

        else -> {
            "세미나 알림"
        }
    }
}
