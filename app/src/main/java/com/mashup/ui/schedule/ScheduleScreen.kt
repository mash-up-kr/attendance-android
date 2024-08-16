package com.mashup.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashup.ui.schedule.component.DailySchedule
import com.mashup.ui.schedule.component.WeeklySchedule
import com.mashup.ui.schedule.model.ScheduleType

@Composable
fun ScheduleScreen(
    scheduleState: ScheduleState,
    modifier: Modifier = Modifier,
    scheduleType: ScheduleType = ScheduleType.WEEK,
    onClickScheduleInformation: (Int, String) -> Unit = { _, _ ->},
    onClickAttendance: (Int) -> Unit = {},
    onClickMashongButton: () -> Unit = {},
    refreshState: Boolean = false
) {
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
