package com.mashup.ui.schedule.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.typography.Body5
import com.mashup.ui.schedule.ScheduleState
import com.mashup.ui.schedule.daily.DailyScheduleByMonth
import com.mashup.core.common.R as CR

@Composable
fun DailySchedule(
    scheduleState: ScheduleState,
    modifier: Modifier = Modifier,
    onClickScheduleInformation: (Int) -> Unit = {}
) {
    var cacheScheduleState by remember {
        mutableStateOf(scheduleState)
    }

    LaunchedEffect(scheduleState) {
        if (scheduleState is ScheduleState.Success) {
            cacheScheduleState = scheduleState
        }
    }

    (cacheScheduleState as? ScheduleState.Success)?.let { state ->
        if (state.monthlyScheduleList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(200.dp))
                Image(
                    painter = painterResource(id = CR.drawable.img_placeholder_sleeping),
                    contentDescription = null,
                    modifier = Modifier.size(88.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "열심히 일정을 준비하고 있어요\n조금만 기다려 주세요!",
                    style = Body5.copy(color = Gray600),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(modifier = Modifier.height(22.dp))
            state.monthlyScheduleList.forEach { (title, scheduleList) ->
                DailyScheduleByMonth(
                    title = title,
                    scheduleList = scheduleList,
                    modifier = Modifier
                        .background(Gray50)
                        .padding(horizontal = 20.dp),
                    isWeeklySchedule = { scheduleId ->
                        state.weeklySchedule.any { scheduleId == it.getScheduleId() }
                    },
                    onClickScheduleInformation = onClickScheduleInformation
                )
            }
        }
    }
}
