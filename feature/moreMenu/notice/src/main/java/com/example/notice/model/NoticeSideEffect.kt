package com.example.notice.model

import com.mashup.core.network.dto.PushHistoryResponse

sealed interface NoticeSideEffect {
    object OnBackPressed : NoticeSideEffect
    data class OnNavigateMenu(val notice: PushHistoryResponse.Notice) : NoticeSideEffect
}
