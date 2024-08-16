package com.mashup.ui.schedule.detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.typography.SubTitle2

@Composable
fun ScheduleDetailLocationItem(content: String) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "공지", style = SubTitle2, color = Gray700)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = content, style = Body5, color = Gray600)
    }
}
