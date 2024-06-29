package com.mashup.ui.schedule.daily.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.ui.schedule.detail.composable.ScheduleInfoText
import com.mashup.core.common.R as CR

/**
 *  TotalScheduleItem.kt
 *
 *  Created by Minji Jeong on 2024/06/29
 *  Copyright © 2024 MashUp All rights reserved.
 */

@Composable
fun DailyScheduleItem(
    title: String,
    time: String,
    place: String,
    highlight: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = if (highlight) {
        listOf(Brand500, Color(0xFF31C1FF))
    } else {
        listOf(Gray100, Gray100)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .border(
                width = if (highlight) (1.5).dp else 1.dp,
                brush = Brush.horizontalGradient(borderColor),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(text = title, style = SubTitle1)
        Spacer(modifier = Modifier.height(4.dp))
        ScheduleInfoText(iconRes = CR.drawable.ic_clock, info = time)
        Spacer(modifier = Modifier.height(4.dp))
        ScheduleInfoText(iconRes = CR.drawable.ic_mappin, info = place)
    }
}

@Preview
@Composable
fun PreviewDailyScheduleItemHighlight() {
    MashUpTheme {
        DailyScheduleItem(
            "안드로이드 팀 세미나",
            "오후 3:00 - 오전 7:00",
            "디스코드",
            true
        )
    }
}

@Preview
@Composable
fun PreviewDailyScheduleItem() {
    MashUpTheme {
        DailyScheduleItem(
            "안드로이드 팀 세미나",
            "오후 3:00 - 오전 7:00",
            "디스코드",
            false
        )
    }
}
