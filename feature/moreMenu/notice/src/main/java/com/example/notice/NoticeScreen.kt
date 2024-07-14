package com.example.notice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.notice.components.NoticeItem
import com.example.notice.model.NoticeSideEffect
import com.example.notice.model.NoticeState
import com.mashup.core.ui.typography.Title3
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun NoticeRoute(
    noticeViewModel: NoticeViewModel,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    val noticeState by noticeViewModel.noticeState.collectAsState()

    LaunchedEffect(Unit) {
        noticeViewModel.noticeEvent.collect {
            when (it) {
                is NoticeSideEffect.OnBackPressed -> onBackPressed()
            }
        }
    }
    NoticeScreen(
        modifier = modifier,
        noticeState = noticeState,
        onBackPressed = noticeViewModel::onBackPressed,
        onLoadNextNotice = noticeViewModel::onLoadNextNotice,
        onReadNewNoticeList = noticeViewModel::onReadNewNoticeList
    )
}

@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    noticeState: NoticeState = NoticeState(),
    onBackPressed: () -> Unit = {},
    onLoadNextNotice: () -> Unit = {},
    onReadNewNoticeList: () -> Unit = {}
) {
    DisposableEffect(Unit) {
        onDispose {
            onReadNewNoticeList()
        }
    }
    Column(modifier = modifier) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = "알림",
            showBackButton = true,
            onClickBackButton = onBackPressed
        )

        val scrollState = rememberLazyListState()
        val lastItemReached by remember {
            derivedStateOf {
                val lastVisibleItem = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()
                val lastItemIndex = scrollState.layoutInfo.totalItemsCount - 1
                lastVisibleItem?.index == lastItemIndex
            }
        }

        LaunchedEffect(lastItemReached) {
            if (lastItemReached) {
                onLoadNextNotice()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp
                ),
            state = scrollState
        ) {
            if (noticeState.newNoticeList.isNotEmpty()) {
                item {
                    Text(
                        text = "새로운 알림",
                        style = Title3,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3037)
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
                items(noticeState.newNoticeList) {
                    NoticeItem(
                        notice = it,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }

            if (noticeState.oldNoticeList.isNotEmpty()) {
                item {
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
                item {
                    Text(
                        text = "오래된 알림",
                        style = Title3,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3037)
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
                items(noticeState.oldNoticeList) {
                    NoticeItem(
                        notice = it,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
        }
    }
}
