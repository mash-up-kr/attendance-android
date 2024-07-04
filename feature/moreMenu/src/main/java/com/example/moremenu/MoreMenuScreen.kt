package com.example.moremenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moremenu.components.MoreMenuItem
import com.example.moremenu.model.Menu
import com.example.moremenu.model.MoreMenuSideEffect
import com.example.moremenu.model.MoreMenuState
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun MoreMenuRoute(
    moreMenuViewModel: MoreMenuViewModel,
    modifier: Modifier = Modifier,
    onNavigateBackStack: () -> Unit = {}
) {
    val moreMenuState by moreMenuViewModel.moreMenuState.collectAsState()
    LaunchedEffect(Unit) {
        moreMenuViewModel.moreMenuEvent.collect { sideEffect ->
            when (sideEffect) {
                is MoreMenuSideEffect.NavigateMenu -> {
                }

                is MoreMenuSideEffect.NavigateBackStack -> onNavigateBackStack()
            }
        }
    }
    MoreMenuScreen(
        modifier = modifier, moreMenuState = moreMenuState,
        onClickMenu = { menu -> },
        onBackPressed = moreMenuViewModel::onClickBackButton
    )
}

@Composable
fun MoreMenuScreen(
    modifier: Modifier = Modifier,
    moreMenuState: MoreMenuState = MoreMenuState(),
    onClickMenu: (Menu) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        item {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "더 보기",
                showBackButton = true,
                onClickBackButton = { onBackPressed() }
            )
        }
        items(moreMenuState.menus) { menu ->
            MoreMenuItem(
                menu = menu,
                onClickMenu = onClickMenu
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Gray100,
                thickness = 1.dp
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMoreMenuScreen() {
    MashUpTheme {
        MoreMenuScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            moreMenuState = MoreMenuState(
                menus = Menu.menuList
            )
        )
    }
}
