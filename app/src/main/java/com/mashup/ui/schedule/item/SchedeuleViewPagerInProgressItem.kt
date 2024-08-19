package com.mashup.ui.schedule.item

import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.mashup.R
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.data.dto.ScheduleResponse
import com.mashup.ui.schedule.model.ScheduleCard
import com.mashup.ui.schedule.util.getBackgroundColor
import com.mashup.ui.schedule.util.getBorderColor
import com.mashup.ui.schedule.util.getButtonTextColor
import java.util.Date

@Composable
fun ScheduleViewPagerInProgressItem(
    data: ScheduleCard.InProgressSchedule,
    modifier: Modifier = Modifier,
    onClickScheduleInformation: (Int, String) -> Unit = { _, _ -> },
    onClickAttendance: (Int) -> Unit = {}
) {
    val textColor by remember { mutableStateOf(data.scheduleResponse.scheduleType.getButtonTextColor()) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = data.scheduleResponse.scheduleType.getBackgroundColor(),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = data.scheduleResponse.scheduleType.getBorderColor(),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClickScheduleInformation(data.scheduleResponse.scheduleId, data.scheduleResponse.scheduleType)
            }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardInfoItem(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            platform = data.scheduleResponse.scheduleType,
            title = data.scheduleResponse.name,
            calendar = data.scheduleResponse.getDate(),
            timeLine = data.scheduleResponse.getTimeLine(),
            location = data.scheduleResponse.location?.detailAddress ?: ""
        )
        Spacer(modifier = Modifier.height(16.dp))

        ConstraintLayout {
            val (button, schedule) = createRefs()

            Column(
                modifier = Modifier
                    .background(color = Color(0xFFE1F2FA), shape = RoundedCornerShape(16.dp))
                    .height(176.dp)
                    .padding(top = 12.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .constrainAs(schedule) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = com.mashup.core.common.R.drawable.img_standby),
                    contentDescription = null
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            ParagraphStyle(lineHeight = 19.09.sp)
                        ) {
                            withStyle(
                                SpanStyle(
                                    color = Color(0xFF4D535E),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W500
                                )
                            ) {
                                append(data.attendanceInfo?.memberName ?: "알 수 없음")
                            }
                            withStyle(
                                SpanStyle(
                                    color = Color(0xFFABB2C1),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W400
                                )
                            ) {
                                append("님의\n참석을 기다리고 있어요.")
                            }
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = Color(0xFFC2EBFF),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(schedule.bottom, 14.dp)
                    },
                factory = { context ->
                    AppCompatTextView(context).apply {
                        text = context.getString(R.string.click_attendance_list)
                        setTextAppearance(
                            com.mashup.core.common.R.style.TextAppearance_Mashup_Body3_14_M
                        )
                        gravity = Gravity.CENTER
                        setTextColor(
                            textColor.toArgb()
                        )
                        setPadding(12, 0, 0, 0)
                        setOnClickListener {
                            onClickAttendance(data.scheduleResponse.scheduleId)
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewScheduleViewPagerEmptySchedule() {
    MashUpTheme {
        Box(
            modifier = Modifier
                .width(294.dp)
                .height(479.dp)
        ) {
            ScheduleViewPagerInProgressItem(
                data = ScheduleCard.InProgressSchedule(
                    scheduleResponse = ScheduleResponse(
                        scheduleId = 0,
                        dateCount = 1,
                        generationNumber = 13,
                        name = "Preview",
                        eventList = emptyList(),
                        startedAt = Date(),
                        endedAt = Date(),
                        location = ScheduleResponse.Location(
                            latitude = 0.0,
                            longitude = 0.0,
                            roadAddress = null,
                            detailAddress = null
                        ),
                        scheduleType = "ALL",
                        notice = null
                    ),
                    attendanceInfo = null
                )
            )
        }
    }
}
