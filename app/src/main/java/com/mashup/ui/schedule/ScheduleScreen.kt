package com.mashup.ui.schedule

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mashup.ui.schedule.component.DailySchedule
import com.mashup.ui.schedule.component.WeeklySchedule
import com.mashup.ui.schedule.model.ScheduleType

@Composable
fun ScheduleScreen(
    scheduleState: ScheduleState,
    dailyListState: LazyListState,
    modifier: Modifier = Modifier,
    scheduleType: ScheduleType = ScheduleType.WEEK,
    onClickScheduleInformation: (Int) -> Unit = {},
    onClickAttendance: (Int) -> Unit = {},
    onClickMashongButton: () -> Unit = {},
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
                onClickScheduleInformation = onClickScheduleInformation,
                onClickAttendance = onClickAttendance,
                onClickMashongButton = onClickMashongButton,
                refreshState = refreshState
            )
        }

        ScheduleType.TOTAL -> {
            DailySchedule(
                scheduleState = scheduleState,
                modifier = modifier,
                onClickScheduleInformation = onClickScheduleInformation
            )
        }
    }
}
