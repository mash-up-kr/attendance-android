package com.example.moremenu.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moremenu.model.Menu
import com.example.moremenu.model.MenuType
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.typography.SubTitle2

@Composable
fun MoreMenuItem(
    menu: Menu,
    modifier: Modifier = Modifier,
    isShowNewIcon: Boolean = false,
    @DrawableRes additionalIcon: Int = 0,
    onClickMenu: (Menu) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable {
            onClickMenu(menu)
        }.padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = menu.type.icon),
            contentDescription = null
        )
        Text(
            text = stringResource(id = menu.type.title),
            style = SubTitle2,
            color = Gray900
        )
        if (menu.type == MenuType.NOTI && isShowNewIcon) {
            Image(
                painter = painterResource(id = additionalIcon),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMoreMenuItem() {
    Column {
        Menu.menuList.forEach {
            MoreMenuItem(menu = it)
        }
    }
}
