package com.mashup.feature.mypage.profile.model

import com.mashup.core.model.Platform

data class ProfileCardData(
    val id: Int = 0,
    val name: String,
    val isRunning: Boolean,
    val generationNumber: Int = 0,
    val platform: Platform,
    val projectTeamName: String = "",
    val role: String = ""
)
