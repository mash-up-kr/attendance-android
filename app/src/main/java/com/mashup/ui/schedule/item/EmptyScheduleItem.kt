package com.mashup.ui.schedule.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.common.R as CommonR

@Composable
fun EmptyScheduleItem(
    modifier: Modifier = Modifier,
    onClickMashongButton: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp),
    ) {
        Image(
            painter = painterResource(id = CommonR.drawable.img_empty_schdule),
            contentDescription = null,
            modifier =
                Modifier
                    .width(284.dp)
                    .height(256.dp),
        )

        Text(
            text = "이번주는 자유다..!",
            style = Header2,
            color = Color(0xFF412491),
        )
        MashUpButton(text = "매숑이 밥주러 가기", buttonHeight = 48.dp, onClick = onClickMashongButton)
    }
}

@Preview
@Composable
private fun PreviewEmptyScheduleItem() {
    MashUpTheme {
        EmptyScheduleItem()
    }
}
