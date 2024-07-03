package com.mashup.ui.schedule.detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Header1
import com.mashup.core.common.R as CR

@Composable
fun ScheduleDetailInfoItem(
    title: String,
    date: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = title, style = Header1)
        Spacer(modifier = Modifier.height(12.dp))

        ScheduleInfoText(iconRes = CR.drawable.ic_calender, info = date)
        Spacer(modifier = Modifier.height(8.dp))

        ScheduleInfoText(iconRes = CR.drawable.ic_clock, info = time)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun PreviewScheduleDetailInfoItem() {
    MashUpTheme {
        ScheduleDetailInfoItem(
            title = "1차 정기 세미나",
            date = "3월 27일",
            time = "오후 3:00 - 오전 7:00"
        )
    }
}
