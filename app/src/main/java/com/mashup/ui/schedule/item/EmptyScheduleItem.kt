package com.mashup.ui.schedule.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Header2
import com.mashup.core.ui.widget.MashUpGradientButton
import com.mashup.core.common.R as CommonR

@Composable
fun EmptyScheduleItem(
    modifier: Modifier = Modifier,
    onClickMashongButton: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier.height(34.dp)
        )
        Image(
            painter = painterResource(id = CommonR.drawable.img_empty_schdule),
            contentDescription = null,
            modifier = Modifier
                .width(284.dp)
                .height(256.dp)
        )
        Spacer(
            modifier = Modifier.height(17.dp)
        )
        Text(
            text = "이번주는 자유다..!",
            style = Header2,
            color = Color(0xFF412491)
        )
        Spacer(
            modifier = Modifier.height(17.dp)
        )
        MashUpGradientButton(
            modifier = Modifier
                .width(256.dp)
                .height(48.dp),
            text = "매숑이 밥주러 가기",
            onClick = onClickMashongButton,
            gradientColors = listOf(
                Color(0xFFB398FE),
                Color(0xFF47BBF1)
            )
        )
    }
}

@Preview
@Composable
private fun PreviewEmptyScheduleItem() {
    MashUpTheme {
        Box(
            modifier = Modifier.background(color = Color.White)
        ) {
            EmptyScheduleItem()
        }
    }
}
