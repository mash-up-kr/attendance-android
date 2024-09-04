package com.mashup.ui.schedule.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body1
import com.mashup.ui.schedule.model.ScheduleCard
import com.mashup.ui.schedule.util.getBackgroundColor
import com.mashup.ui.schedule.util.getBorderColor

@Composable
fun ScheduleViewPagerEmptyItem(
    data: ScheduleCard.EmptySchedule,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().wrapContentHeight().background(
            color = data.scheduleResponse?.scheduleType?.getBackgroundColor() ?: Color(0xFFECF9FF),
            shape = RoundedCornerShape(20.dp)
        )
            .border(
                width = 1.dp,
                color = data.scheduleResponse?.scheduleType?.getBorderColor() ?: Color(0xFFE1F2FA),
                shape = RoundedCornerShape(20.dp)
            ).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardInfoItem(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            platform = data.scheduleResponse?.scheduleType ?: "ALL",
            title = data.scheduleResponse?.name ?: "",
            calendar = data.scheduleResponse?.getDate() ?: "-",
            timeLine = data.scheduleResponse?.getTimeLine() ?: "-",
            location = "-"
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth().background(
                color = Color(0xFFE1F2FA),
                shape = RoundedCornerShape(16.dp)
            ).padding(
                vertical = 22.dp,
                horizontal = 20.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(vertical = 66.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        com.mashup.core.common.R.drawable.img_placeholder_sleeping
                    ),
                    contentDescription = null
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                Text(
                    text = stringResource(id = R.string.description_empty_schedule),
                    style = Body1,
                    color = Gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewEmptySchedule() {
    MashUpTheme {
        ScheduleViewPagerEmptyItem(data = ScheduleCard.EmptySchedule())
    }
}
