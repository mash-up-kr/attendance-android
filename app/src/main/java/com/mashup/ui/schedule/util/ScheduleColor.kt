package com.mashup.ui.schedule.util

import androidx.compose.ui.graphics.Color
import com.mashup.core.ui.widget.PlatformType


fun String.getEventTimelineBackgroundColor(): Color {
    return when (this) {
        "ALL" -> Color(0xFFE1F2FA)
        else -> Color(0xFFF5F1FF)
    }
}

fun String.getButtonBackgroundColor(): Color {
    return when (this) {
        "ALL" -> Color(0xFFC2EBFF)
        else -> Color(0xFFE7DEFF)
    }
}

fun String.getBackgroundColor(): Color {
    return when (this) {
        "ALL" -> Color(0xFFECF9FF)
        else -> Color(0xFFF5F1FF)
    }
}

fun String.getBorderColor(): Color {
    return when (this) {
        "ALL" -> Color(0xFFE1F2FA)
        else -> Color(0xFFE7DEFF).copy(
            alpha = 0.3f
        )
    }
}

fun String.getButtonTextColor(): Color {
    return when (this) {
        "ALL" -> Color(0xFF358CB6)
        else -> Color(0xFF6A36FF)
    }

}


fun String.convertCamelCase(): PlatformType {
    return when (this) {
        "ALL" -> PlatformType.Semina
        "DESIGN" -> PlatformType.Design
        "SPRING" -> PlatformType.Spring
        "IOS" -> PlatformType.Ios
        "ANDROID" -> PlatformType.Android
        "WEB" -> PlatformType.Web
        "NODE" -> PlatformType.Node
        else -> PlatformType.Semina
    }
}

