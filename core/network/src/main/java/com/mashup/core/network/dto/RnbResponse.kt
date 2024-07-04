package com.mashup.core.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RnbResponse(
    val menus: List<String>
)
