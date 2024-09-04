package com.example.moremenu.model

import androidx.annotation.DrawableRes
import com.mashup.core.ui.R

data class MoreMenuState(
    val menus: List<Menu> = emptyList(),
    val isShowNewIcon: Boolean = false,
    @DrawableRes val additionalIcon: Int = R.drawable.ic_new
)
