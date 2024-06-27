package com.mashup.ui.schedule.detail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.Caption1
import com.mashup.core.ui.typography.Caption2
import com.mashup.core.ui.typography.SubTitle2

@Composable
fun ScheduleDetailContentItem(
    contentId: String,
    title: String,
    content: String,
    time: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Gray100),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contentId,
                    style = Caption2,
                    color = Gray600
                )
            }

            Text(
                text = title,
                style = SubTitle2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Text(
                text = time,
                style = Caption1,
                color = Gray400
            )
        }

        if (content.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = Body4,
                color = Gray600,
                modifier = Modifier.padding(start = 28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleDetailContentItem() {
    MashUpTheme {
        ScheduleDetailContentItem(
            contentId = "1",
            title = "안드로이드 팀 세미나",
            content = "Android Crew",
            time = "AM 11:00"
        )
    }
}
