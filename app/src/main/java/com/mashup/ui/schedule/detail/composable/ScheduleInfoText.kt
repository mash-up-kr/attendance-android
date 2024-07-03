package com.mashup.ui.schedule.detail.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray300
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.common.R as CR

@Composable
fun ScheduleInfoText(
    @DrawableRes iconRes: Int,
    info: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = info,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(Gray300)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(text = info, style = Body4, color = Gray700)
    }
}

@Preview
@Composable
fun PreviewScheduleInfoText() {
    MashUpTheme {
        ScheduleInfoText(
            iconRes = CR.drawable.ic_calender,
            info = "3월 7일"
        )
    }
}
