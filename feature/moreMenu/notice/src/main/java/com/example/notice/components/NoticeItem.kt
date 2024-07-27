package com.example.notice.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notice.util.getNoticeTime
import com.example.notice.util.getPushImage
import com.mashup.core.network.dto.PushHistoryResponse
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.typography.Caption1
import com.mashup.core.ui.typography.Caption2

@Composable
fun NoticeItem(
    notice: PushHistoryResponse.Notice,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .size(26.dp)
                .align(Alignment.Top),
            painter = painterResource(notice.getPushImage()),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .width(254.dp)
        ) {
            Text(
                text = notice.title,
                style = Caption1,
                color = Gray500
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                text = notice.body,
                style = Body5.copy(
                    fontWeight = FontWeight.W600
                ),
                color = Gray950
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = notice.sendTime.getNoticeTime(),
                style = Caption2,
                color = Gray400
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNoticeItem() {
    NoticeItem(
        modifier = Modifier.background(color = Color.White),
        notice = PushHistoryResponse.Notice(
            title = "",
            body = "",
            sendTime = "",
            pushType = "",
            linkType = ""
        )
    )
}
