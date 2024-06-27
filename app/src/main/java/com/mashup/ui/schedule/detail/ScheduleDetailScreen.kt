package com.mashup.ui.schedule.detail

import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.ui.schedule.detail.composable.ScheduleDetailContentItem
import com.mashup.ui.schedule.detail.composable.ScheduleDetailHeaderItem
import com.mashup.ui.schedule.detail.composable.ScheduleDetailInfoItem
import com.mashup.ui.schedule.detail.composable.ScheduleDetailLocationItem
import com.mashup.ui.schedule.model.EventDetail

@Composable
fun ScheduleDetailScreen(
    state: ScheduleState,
    copyToClipboard: (String) -> Unit,
    moveToPlatformAttendance: () -> Unit,
    onBackPressed: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            MashUpToolbar(
                title = "상세 스케쥴",
                showBackButton = true,
                onClickBackButton = onBackPressed
            )

            when (state) {
                is ScheduleState.Empty -> {}
                is ScheduleState.Success -> EventDetailList(state.eventDetailList, copyToClipboard)
                else -> {}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )

        MashUpButton(
            text = "플랫폼 출석현황 보러가기",
            onClick = moveToPlatformAttendance,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, bottom = 28.dp, end = 20.dp)
                .align(Alignment.BottomCenter),
            buttonStyle = ButtonStyle.INVERSE
        )
    }
}

@Composable
fun EventDetailList(eventDetailList: List<EventDetail>, copyToClipboard: (String) -> Unit) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(start = 20.dp, bottom = 60.dp, end = 20.dp)
        ) {
            itemsIndexed(eventDetailList) { _, item ->
                when (item) {
                    is EventDetail.Info -> {
                        ScheduleDetailInfoItem(
                            title = item.title,
                            date = item.date,
                            time = item.title
                        )
                    }

                    is EventDetail.Location -> {
                        ScheduleDetailLocationItem(
                            detailAddress = item.detailAddress,
                            roadAddress = item.roadAddress,
                            latitude = item.latitude,
                            longitude = item.longitude,
                            copyToClipboard = copyToClipboard
                        )
                    }

                    is EventDetail.Header -> {
                        ScheduleDetailHeaderItem(
                            isFirstEvent = item.eventId == 1,
                            title = item.title,
                            time = item.formattedTime
                        )
                    }

                    is EventDetail.Content -> {
                        ScheduleDetailContentItem(
                            contentId = item.contentId,
                            title = item.title,
                            content = item.content,
                            time = item.formattedTime
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewScheduleDetailScreen() {
    MashUpTheme {
        ScheduleDetailScreen(
            state = ScheduleState.Empty,
            copyToClipboard = {},
            moveToPlatformAttendance = {},
            onBackPressed = {}
        )
    }
}
