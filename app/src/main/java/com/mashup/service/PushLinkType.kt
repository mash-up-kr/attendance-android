package com.mashup.service

enum class PushLinkType {
    MAIN, // 기본 페이지
    QR, // QR 페이지
    DANGGN, // 당근 페이지
    DANGGN_REWARD,
    BIRTHDAY, // 생일 축하
    MASHONG, // 매숑이 키우기
    MYPAGE,
    UNKNOWN
    ;

    companion object {
        fun getPushLinkType(type: String) =
            values().find { it.name == type } ?: UNKNOWN
    }
}
