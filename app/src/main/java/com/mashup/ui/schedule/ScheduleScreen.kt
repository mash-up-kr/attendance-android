package com.mashup.ui.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    scheduleState: ScheduleState,
    modifier: Modifier = Modifier,
    onClickScheduleInformation: (Int) -> Unit = {},
    onClickAttendance: (Int) -> Unit = {},
    refreshState: Boolean = false
) {
    var cacheScheduleState by remember {
        mutableStateOf(scheduleState)
    }

    LaunchedEffect(scheduleState) {
        if (scheduleState is ScheduleState.Success) {
            cacheScheduleState = scheduleState
        }
    }

    when (cacheScheduleState) {
        is ScheduleState.Success -> {
            val castingState = cacheScheduleState as ScheduleState.Success
            val horizontalPagerState = rememberPagerState(
                initialPage = if (castingState.scheduleList.size < 6) 1 else castingState.scheduleList.size - 4,
                pageCount = { castingState.scheduleList.size }
            )
            LaunchedEffect(refreshState) {
                if (refreshState.not()) { // refresh 가 끝났을 경우
                    horizontalPagerState.animateScrollToPage(castingState.schedulePosition)
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
                when (val data = castingState.scheduleList[index]) {
                    is ScheduleCard.EmptySchedule -> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            EmptyScheduleItem(
                                modifier = Modifier.fillMaxSize().graphicsLayer {
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

        else -> {}
    }
}
