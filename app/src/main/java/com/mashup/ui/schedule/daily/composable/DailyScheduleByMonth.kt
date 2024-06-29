package com.mashup.ui.schedule.daily.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.common.extensions.day
import com.mashup.core.common.extensions.week
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.Caption2
import com.mashup.core.ui.typography.Title3
import com.mashup.data.dto.ScheduleResponse

/**
 *  DailyScheduleByMonth.kt
 *
 *  Created by Minji Jeong on 2024/06/29
 *  Copyright © 2024 MashUp All rights reserved.
 */

@Composable
fun DailyScheduleByMonth(
    title: String,
    scheduleList: List<ScheduleResponse>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(title, style = Title3)
        Spacer(modifier = Modifier.height(18.dp))
        scheduleList
            .groupBy { it.startedAt.day() }
            .forEach { (_, dailySchedule) ->
                DailyScheduleByDay(dailySchedule)
            }
    }
}

@Composable
fun DailyScheduleByDay(schedule: List<ScheduleResponse>) {
    Row(modifier = Modifier.padding(bottom = 32.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 14.dp, end = 14.dp)
        ) {
            Text(text = "${schedule[0].startedAt.day()}", style = Body3)
            Text(text = schedule[0].startedAt.week(), style = Caption2.copy(color = Gray500))
        }

        Column {
            schedule.forEach {
                DailyScheduleItem(
                    title = it.name,
                    time = it.getTimeLine(),
                    place = it.location?.detailAddress ?: "-",
                    highlight = true, // TODO
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDailySchedule() {
    MashUpTheme {
        DailyScheduleByMonth("2024년 8월", emptyList())
    }
}
