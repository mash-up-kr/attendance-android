package com.example.moremenu.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mashup.core.network.dto.RnbResponse
import com.mashup.feature.moreMenu.R
import com.mashup.core.ui.R as CoreR

sealed interface Menu {
    val menuName: String
    val type: MenuType

    data class BirthDay(
        override val menuName: String = MenuType.BIRTHDAY.name,
        override val type: MenuType = MenuType.BIRTHDAY
    ) : Menu

    data class Mashong(
        override val menuName: String = MenuType.MASHONG.name,
        override val type: MenuType = MenuType.MASHONG
    ) : Menu

    data class Danggn(
        override val menuName: String = MenuType.DANGGN.name,
        override val type: MenuType = MenuType.DANGGN
    ) : Menu

    data class Noti(
        override val menuName: String = MenuType.NOTI.name,
        override val type: MenuType = MenuType.NOTI

    ) : Menu

    data class Setting(
        override val menuName: String = MenuType.SETTING.name,
        override val type: MenuType = MenuType.SETTING
    ) : Menu

    companion object {
        val menuList = listOf(
            BirthDay(),
            Mashong(),
            Danggn(),
            Noti(),
            Setting()
        )

        fun RnbResponse.toMenu(): List<Menu> {
            return menuList.filter { menu -> this.menus.contains(menu.type.name) }
        }
    }
}

enum class MenuType(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    BIRTHDAY(
        title = R.string.menu_title_birthday,
        icon = CoreR.drawable.ic_birthday
    ),
    MASHONG(
        title = R.string.menu_title_mashong,
        icon = CoreR.drawable.ic_mashong
    ),
    DANGGN(
        title = R.string.menu_title_danggn,
        icon = CoreR.drawable.ic_carrot
    ),
    NOTI(
        title = R.string.menu_title_notice,
        icon = CoreR.drawable.ic_alarm
    ),
    SETTING(
        title = R.string.menu_title_setting,
        icon = CoreR.drawable.ic_setting
    )
}
