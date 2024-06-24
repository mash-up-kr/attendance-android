package com.mashup.ui.schedule

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.R
import com.mashup.constant.log.LOG_SCHEDULE_EVENT_DETAIL
import com.mashup.constant.log.LOG_SCHEDULE_STATUS_CONFIRM
import com.mashup.core.common.extensions.fromHtml
import com.mashup.core.ui.colors.Brand500
import com.mashup.ui.attendance.platform.PlatformAttendanceActivity
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.main.MainViewModel
import com.mashup.ui.main.model.MainPopupType
import com.mashup.ui.schedule.detail.ScheduleDetailActivity
import com.mashup.ui.schedule.item.ScheduleViewPagerEmptyItem
import com.mashup.ui.schedule.item.ScheduleViewPagerInProgressItem
import com.mashup.ui.schedule.item.ScheduleViewPagerSuccessItem
import com.mashup.ui.schedule.model.ScheduleCard
import com.mashup.util.AnalyticsManager
import com.mashup.util.debounce
import kotlin.math.abs
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleRoute(
    mainViewModel: MainViewModel,
    viewModel: ScheduleViewModel,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            // refresh api
            viewModel.getScheduleList()
        }
    )
    val context = LocalContext.current

    val state by viewModel.scheduleState.collectAsState()

    var title by remember { mutableStateOf("") }

    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            mainViewModel.onAttendance.collect {
                viewModel.getScheduleList()
            }
        }
    }

    LaunchedEffect(state) {
        when (state) {
            is ScheduleState.Loading -> {}
            is ScheduleState.Empty -> { isRefreshing = false }
            is ScheduleState.Success -> {
                isRefreshing = false
                title = context.setUiOfScheduleTitle(
                    (state as ScheduleState.Success).scheduleTitleState
                )
            }
            is ScheduleState.Error -> { isRefreshing = false }
        }
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AndroidView(
                    factory = { context ->
                        AppCompatTextView(
                            context
                        ).apply {
                            setTextAppearance(
                                com.mashup.core.common.R.style.TextAppearance_Mashup_Header1_24_B
                            )
                            text = title
                        }
                    },
                    update = { view ->
                        view.text = title
                    }
                )
                if (title.isNotEmpty()) {
                    val coroutineScope = rememberCoroutineScope()
                    Image(
                        modifier = Modifier.clickable {
                            debounce<Unit>(500L, scope = coroutineScope, destinationFunction = {
                                mainViewModel.disablePopup(MainPopupType.DANGGN)
                            })
                            context.startActivity(
                                ShakeDanggnActivity.newIntent(context)
                            )
                        },
                        painter = painterResource(
                            id = com.mashup.core.common.R.drawable.img_carrot_button
                        ),
                        contentDescription = null
                    )
                }
            }
            when (state) {
                is ScheduleState.Error -> {}
                is ScheduleState.Empty -> {}
                else -> {
                    ScheduleScreen(
                        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                        scheduleState = state,
                        onClickScheduleInformation = { scheduleId: Int ->
                            AnalyticsManager.addEvent(eventName = LOG_SCHEDULE_EVENT_DETAIL)
                            context.startActivity(
                                ScheduleDetailActivity.newIntent(context, scheduleId)
                            )
                        },
                        onClickAttendance = { scheduleId: Int ->
                            AnalyticsManager.addEvent(eventName = LOG_SCHEDULE_STATUS_CONFIRM)
                            context.startActivity(
                                PlatformAttendanceActivity.newIntent(context, scheduleId)
                            )
                        },
                        refreshState = isRefreshing

                    )
                }
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            scale = true,
            contentColor = Brand500,
            refreshing = isRefreshing,
            state = pullRefreshState
        )
    }
}

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
                            ScheduleViewPagerEmptyItem(
                                modifier = Modifier
                                    .graphicsLayer {
                                        val pageOffset = (
                                            (horizontalPagerState.currentPage - index) + horizontalPagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue
                                        scaleY = 1 - 0.1f * abs(pageOffset)
                                    },
                                data = data
                            )
                        }
                    }
                    is ScheduleCard.EndSchedule -> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ScheduleViewPagerSuccessItem(
                                modifier = Modifier
                                    .graphicsLayer {
                                        val pageOffset = (
                                            (horizontalPagerState.currentPage - index) + horizontalPagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue
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
                                modifier = Modifier
                                    .graphicsLayer {
                                        val pageOffset = (
                                            (horizontalPagerState.currentPage - index) + horizontalPagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue
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
fun Context.setUiOfScheduleTitle(scheduleTitleState: ScheduleTitleState): String {
    return when (scheduleTitleState) {
        ScheduleTitleState.Empty -> {
            getString(R.string.empty_schedule)
        }
        is ScheduleTitleState.End -> {
            getString(R.string.end_schedule, scheduleTitleState.generatedNumber)
        }
        is ScheduleTitleState.DateCount -> {
            getString(R.string.event_list_title, scheduleTitleState.dataCount).fromHtml().toString()
        }
        is ScheduleTitleState.SchedulePreparing -> {
            getString(R.string.preparing_attendance)
        }
    }
}
