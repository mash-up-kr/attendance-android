package com.example.notice.model

sealed interface NoticeSideEffect {
    object OnBackPressed : NoticeSideEffect
}
