package com.mashup.feature.mypage.profile.model

import com.mashup.core.model.Platform

data class MyProfileCardEntity(
    val id: Int = 0,
    val generationNumber: Int = 0,
    val platform: Platform = Platform.UNKNOWN,
    val team: String = "",
    val staff: String = ""
)
