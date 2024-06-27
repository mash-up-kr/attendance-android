package com.mashup.ui.schedule.detail.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.common.R
import com.mashup.core.ui.colors.Brand100
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Caption1
import com.mashup.core.ui.typography.Header2

@Composable
fun ScheduleDetailHeaderItem(
    isFirstEvent: Boolean,
    title: String,
    time: String
) {
    Column {
        if (isFirstEvent.not()) {
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Gray100, thickness = 1.dp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = Header2)

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Brand100)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = time,
                    style = Caption1,
                    color = Brand500
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScheduleDetailHeaderItem() {
    MashUpTheme {
        ScheduleDetailHeaderItem(
            isFirstEvent = false,
            title = "1부",
            time = "10:00 - 11:00"
        )
    }
}
