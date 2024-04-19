package com.mashup.feature.mypage.profile.model

sealed class LoadState {
    object Initial : LoadState()
    object Loading : LoadState()
    object Loaded : LoadState()
}
