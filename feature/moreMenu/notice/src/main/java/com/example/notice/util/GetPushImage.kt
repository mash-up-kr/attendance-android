package com.example.notice.util

import androidx.annotation.DrawableRes
import com.mashup.core.network.dto.PushHistoryResponse

@DrawableRes
fun PushHistoryResponse.Notice.getPushImage(): Int {
    return when (pushType) {
        "BIRTHDAY" -> {
            com.mashup.core.ui.R.drawable.ic_birthday
        }

        "MASHONG" -> {
            com.mashup.core.ui.R.drawable.ic_mashong
        }

        "DANGGN" -> {
            com.mashup.core.ui.R.drawable.ic_carrot
        }

        "NOTI" -> {
            com.mashup.core.ui.R.drawable.ic_alarm
        }

        "SETTING" -> {
            com.mashup.core.ui.R.drawable.ic_setting
        }

        else -> {
            com.mashup.core.ui.R.drawable.ic_etc
        }
    }
}
