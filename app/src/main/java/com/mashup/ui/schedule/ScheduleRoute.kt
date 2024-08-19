package com.mashup.ui.schedule

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.R
import com.mashup.constant.log.LOG_EVENT_LIST_EVENT_DETAIL
import com.mashup.constant.log.LOG_EVENT_LIST_STATUS_CONFIRM
import com.mashup.core.common.extensions.fromHtml
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpHtmlText
import com.mashup.ui.attendance.platform.PlatformAttendanceActivity
import com.mashup.ui.main.MainViewModel
import com.mashup.ui.schedule.component.ScheduleTabRow
import com.mashup.ui.schedule.detail.ScheduleDetailActivity
import com.mashup.ui.schedule.model.ScheduleType
import com.mashup.ui.webview.mashong.MashongActivity
import com.mashup.util.AnalyticsManager
import com.mashup.core.common.R as CR

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleRoute(
    mainViewModel: MainViewModel,
    viewModel: ScheduleViewModel,
    modifier: Modifier = Modifier,
    onClickMoreMenuIcon: () -> Unit = {},
    makeToast: (String) -> Unit = {}
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.getScheduleList() // refresh api
        }
    )

    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            mainViewModel.onAttendance.collect {
                viewModel.getScheduleList()
            }
        }
    }

    val scheduleState by viewModel.scheduleState.collectAsState()
    LaunchedEffect(scheduleState) {
        when (val state = scheduleState) {
            is ScheduleState.Loading -> {}
            is ScheduleState.Init -> {
                isRefreshing = false
            }

            is ScheduleState.Success -> {
                isRefreshing = false
                title = context.setUiOfScheduleTitle(state.scheduleTitleState)
            }

            is ScheduleState.Error -> {
                isRefreshing = false
            }
        }
    }

    val weeklyListState = rememberLazyListState()
    val dailyListState = rememberLazyListState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(
            modifier = modifier
                .pullRefresh(pullRefreshState)
                .background(color = if (ScheduleType.values()[selectedTabIndex] == ScheduleType.WEEK) Color.White else Gray50)
        ) {
            LazyColumn(
                modifier = modifier,
                state = if (selectedTabIndex == 0) weeklyListState else dailyListState
            ) {
                item {
                    ScheduleTopbar(
                        title,
                        onClickMoreMenuIcon = onClickMoreMenuIcon
                    )
                    Spacer(
                        modifier = Modifier
                            .height(26.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                }

                stickyHeader {
                    ScheduleTabRow(
                        modifier = Modifier.background(White),
                        selectedTabIndex = selectedTabIndex,
                        updateSelectedTabIndex = { index ->
                            selectedTabIndex = index
                        }
                    )
                }

                item {
                    when (scheduleState) {
                        is ScheduleState.Error -> {}
                        is ScheduleState.Init -> {}
                        else -> {
                            ScheduleScreen(
                                modifier = Modifier.fillMaxSize(),
                                scheduleState = scheduleState,
                                onClickScheduleInformation = { id, type -> context.moveToScheduleInformation(id, type) },
                                dailyListState = dailyListState,
                                onClickAttendance = { context.moveToAttendance(it) },
                                onClickMashongButton = { context.moveToMashong() },
                                makeToast = makeToast,
                                refreshState = isRefreshing,
                                scheduleType = ScheduleType.values()[selectedTabIndex]
                            )
                        }
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
            getString(R.string.event_list_title, scheduleTitleState.dataCount)
        }

        is ScheduleTitleState.SchedulePreparing -> {
            getString(R.string.preparing_attendance)
        }
    }
}

fun Context.moveToScheduleInformation(scheduleId: Int, scheduleType: String) {
    AnalyticsManager.addEvent(eventName = LOG_EVENT_LIST_EVENT_DETAIL)
    startActivity(
        ScheduleDetailActivity.newIntent(this, scheduleId, scheduleType)
    )
}

fun Context.moveToAttendance(scheduleId: Int) {
    AnalyticsManager.addEvent(eventName = LOG_EVENT_LIST_STATUS_CONFIRM)
    startActivity(
        PlatformAttendanceActivity.newIntent(this, scheduleId)
    )
}

fun Context.moveToMashong() {
    startActivity(
        MashongActivity.newIntent(this)
    )
}

@Composable
fun ScheduleTopbar(
    title: String,
    onClickMoreMenuIcon: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(White)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MashUpHtmlText(
            content = title.fromHtml(),
            modifier = Modifier.weight(1f, false),
            textAppearance = CR.style.TextAppearance_Mashup_Header1_24_B
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = CR.drawable.ic_more),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    onClickMoreMenuIcon()
                }
        )
    }
}

@Preview
@Composable
fun PreviewScheduleTopbar() {
    MashUpTheme {
        ScheduleTopbar(title = "다음 세미나 준비 중이에요.\n조금만 기다려주세요.")
    }
}
