package com.example.notice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notice.components.NoticeItem
import com.example.notice.model.NoticeState
import com.example.notice.model.NoticeState.Companion.isNoticeEmpty
import com.mashup.core.network.dto.PushHistoryResponse
import com.mashup.core.ui.R
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.typography.Title3
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun NoticeRoute(
    modifier: Modifier = Modifier,
    noticeState: NoticeState = NoticeState(),
    onBackPressed: () -> Unit = {},
    onLoadNextNotice: () -> Unit = {},
    onClickNoticeItem: (PushHistoryResponse.Notice) -> Unit = {}
) {
    NoticeScreen(
        modifier = modifier,
        noticeState = noticeState,
        onBackPressed = onBackPressed,
        onLoadNextNotice = onLoadNextNotice,
        onClickNoticeItem = onClickNoticeItem
    )
}

@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    noticeState: NoticeState = NoticeState(),
    onBackPressed: () -> Unit = {},
    onLoadNextNotice: () -> Unit = {},
    onClickNoticeItem: (PushHistoryResponse.Notice) -> Unit = {}
) {
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

        if (noticeState.isError) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_error),
                    contentDescription = null
                )
                Text(
                    text = "오류가 발생했어요...",
                    color = Gray600,
                    style = Body5
                )
                return
            }
        }

        if (noticeState.isNoticeEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_noalert),
                    contentDescription = null
                )
                Text(
                    text = "아직 도착한 알림이 없어요",
                    color = Gray600,
                    style = Body5
                )
                return
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
                            .fillMaxWidth().clickable {
                                onClickNoticeItem(it)
                            }
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
                        text = "지난 알림",
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
                            .fillMaxWidth().clickable {
                                onClickNoticeItem(it)
                            }
                    )
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNoticeScreen() {
    MashUpTheme {
        NoticeScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF8F7FC))
        )
    }
}

@Preview
@Composable
private fun PreviewNoticeScreenError() {
    MashUpTheme {
        NoticeScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF8F7FC)),
            noticeState = NoticeState().copy(
                isError = true
            )
        )
    }
}
