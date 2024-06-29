package com.mashup.ui.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mashup.ui.schedule.item.EmptyScheduleItem
import com.mashup.ui.schedule.item.ScheduleViewPagerInProgressItem
import com.mashup.ui.schedule.item.ScheduleViewPagerSuccessItem
import com.mashup.ui.schedule.model.ScheduleCard
import kotlin.math.abs
import kotlin.math.absoluteValue

@Composable
fun ScheduleScreen(
    scheduleState: ScheduleState.Success,
    modifier: Modifier = Modifier,
    onClickScheduleInformation: (Int) -> Unit = {},
    onClickAttendance: (Int) -> Unit = {},
    refreshState: Boolean = false
) {
    val horizontalPagerState = rememberPagerState(
        initialPage = if (scheduleState.scheduleList.size < 6) 1 else scheduleState.scheduleList.size - 4,
        pageCount = { scheduleState.scheduleList.size }
    )

    LaunchedEffect(refreshState) {
        if (refreshState.not()) { // refresh 가 끝났을 경우
            horizontalPagerState.animateScrollToPage(scheduleState.schedulePosition)
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = horizontalPagerState,
        pageSpacing = 12.dp,
        pageSize = PageSize.Fill,
        contentPadding = PaddingValues(33.dp),
        verticalAlignment = Alignment.Top
    ) { index ->
        when (val data = scheduleState.scheduleList[index]) {
            is ScheduleCard.EmptySchedule -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EmptyScheduleItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset =
                                    ((horizontalPagerState.currentPage - index) + horizontalPagerState.currentPageOffsetFraction).absoluteValue
                                scaleY = 1 - 0.1f * abs(pageOffset)
                            }
                    )
                }
            }

            is ScheduleCard.EndSchedule -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ScheduleViewPagerSuccessItem(
                        modifier = Modifier.graphicsLayer {
                            val pageOffset =
                                ((horizontalPagerState.currentPage - index) + horizontalPagerState.currentPageOffsetFraction).absoluteValue
                            scaleY = 1 - 0.1f * abs(pageOffset)
                        },
                        data = data,
                        onClickScheduleInformation = onClickScheduleInformation,
                        onClickAttendance = onClickAttendance
                    )
                }
            }

            is ScheduleCard.InProgressSchedule -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ScheduleViewPagerInProgressItem(
                        modifier = Modifier.graphicsLayer {
                            val pageOffset =
                                ((horizontalPagerState.currentPage - index) + horizontalPagerState.currentPageOffsetFraction).absoluteValue
                            scaleY = 1 - 0.1f * abs(pageOffset)
                        },
                        data = data,
                        onClickScheduleInformation = onClickScheduleInformation,
                        onClickAttendance = onClickAttendance
                    )
                }
            }
        }
    }
}
