package com.mashup.ui.main.model

enum class MainPopupType {
    DANGGN, DANGGN_UPDATE, BIRTHDAY_CELEBRATION, UNKNOWN;

    companion object {
        fun getMainPopupType(type: String): MainPopupType {
            return MainPopupType.values().find { it.name == type } ?: UNKNOWN
        }
    }
}
