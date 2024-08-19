package com.mashup.ui.schedule

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import com.mashup.constant.log.LOG_EVENT_LIST_EVENT_DETAIL
import com.mashup.ui.schedule.component.DailySchedule
import com.mashup.ui.schedule.component.WeeklySchedule
import com.mashup.ui.schedule.model.ScheduleType
import com.mashup.util.AnalyticsManager

@Composable
fun ScheduleScreen(
    scheduleState: ScheduleState,
    dailyListState: LazyListState,
    modifier: Modifier = Modifier,
    scheduleType: ScheduleType = ScheduleType.WEEK,
    onClickScheduleInformation: (Int, String) -> Unit = { _, _ -> },
    onClickAttendance: (Int) -> Unit = {},
    onClickMashongButton: () -> Unit = {},
    makeToast: (String) -> Unit = {},
    refreshState: Boolean = false
) {
    LaunchedEffect(scheduleState) {
        if (scheduleState is ScheduleState.Success) {
            if (scheduleState.schedulePosition < scheduleState.scheduleList.size) {
                dailyListState.animateScrollToItem(scheduleState.schedulePosition)
            }
        }
    }

    when (scheduleType) {
        ScheduleType.WEEK -> {
            WeeklySchedule(
                scheduleState = scheduleState,
                modifier = modifier,
                onClickScheduleInformation = { scheduleId: Int, type: String ->
                    AnalyticsManager.addEvent(
                        eventName = LOG_EVENT_LIST_EVENT_DETAIL,
                        params = bundleOf("place" to "이번주일정")
                    )
                    onClickScheduleInformation(scheduleId, type)
                },
                onClickAttendance = onClickAttendance,
                onClickMashongButton = onClickMashongButton,
                makeToast = makeToast,
                refreshState = refreshState
            )
        }

        ScheduleType.TOTAL -> {
            DailySchedule(
                scheduleState = scheduleState,
                modifier = modifier,
                onClickScheduleInformation = { scheduleId: Int, type: String ->
                    AnalyticsManager.addEvent(
                        eventName = LOG_EVENT_LIST_EVENT_DETAIL,
                        params = bundleOf("place" to "전체일정")
                    )
                    onClickScheduleInformation(scheduleId, type)
                }
            )
        }
    }
}
