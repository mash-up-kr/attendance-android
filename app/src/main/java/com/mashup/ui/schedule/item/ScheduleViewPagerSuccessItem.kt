package com.mashup.ui.schedule.item

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.mashup.R
import com.mashup.core.ui.colors.Brand200
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.typography.Body1
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.typography.Caption1
import com.mashup.core.ui.typography.SubTitle2
import com.mashup.core.ui.widget.PlatformType
import com.mashup.data.dto.EventResponse
import com.mashup.ui.schedule.ViewEventTimeline
import com.mashup.ui.schedule.model.ScheduleCard
import com.mashup.ui.schedule.util.convertCamelCase
import com.mashup.ui.schedule.util.getBackgroundColor
import com.mashup.ui.schedule.util.getBorderColor
import com.mashup.ui.schedule.util.getButtonBackgroundColor
import com.mashup.ui.schedule.util.getButtonTextColor
import com.mashup.ui.schedule.util.getEventTimelineBackgroundColor
import com.mashup.ui.schedule.util.onBindAttendanceImage
import com.mashup.ui.schedule.util.onBindAttendanceStatus
import com.mashup.ui.schedule.util.onBindAttendanceTime

@Composable
fun ScheduleViewPagerSuccessItem(
    data: ScheduleCard.EndSchedule,
    modifier: Modifier = Modifier,
    onClickScheduleInformation: (Int, String) -> Unit = { _, _ -> },
    makeToast: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
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
                if (data.scheduleResponse.notice.isNullOrEmpty() && data.scheduleResponse.eventList.isEmpty()) {
                    makeToast("볼 수 있는 일정이 없어요..!")
                } else {
                    onClickScheduleInformation(
                        data.scheduleResponse.scheduleId,
                        data.scheduleResponse.scheduleType
                    )
                }
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

        if (data.scheduleResponse.scheduleType.convertCamelCase() == PlatformType.Seminar) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .background(
                        color = data.scheduleResponse.scheduleType.getEventTimelineBackgroundColor(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(176.dp)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                itemsIndexed(data.scheduleResponse.eventList, key = { _: Int, item: EventResponse ->
                    item.eventId
                }) { index: Int, _: EventResponse ->
                    Column {
                        if (index == 0) {
                            val spannableString = SpannableStringBuilder(
                                String.format(
                                    context.resources.getString(
                                        R.string.event_list_card_title
                                    ),
                                    data.attendanceInfo.memberName
                                )
                            ).toString()

                            Text(
                                text = HtmlCompat.fromHtml(
                                    spannableString,
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                ).toAnnotatedString(),
                                color = Gray700,
                                style = Body1
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )
                        }
                        ViewEventTimeline(
                            modifier = Modifier.fillMaxWidth(),
                            caption = stringResource(id = R.string.attendance_caption, index + 1),
                            time = onBindAttendanceTime(data.attendanceInfo.getAttendanceAt(index)),
                            status = onBindAttendanceStatus(
                                data.attendanceInfo.getAttendanceStatus(index)
                            ),
                            image = onBindAttendanceImage(
                                data.attendanceInfo.getAttendanceStatus(index)
                            )
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )
                }
                item {
                    val status = data.attendanceInfo.getFinalAttendance()
                    ViewEventTimeline(
                        modifier = Modifier.fillMaxWidth(),
                        caption = stringResource(id = R.string.attendance_final),
                        status = onBindAttendanceStatus(status, isFinal = true),
                        image = onBindAttendanceImage(status, isFinal = true),
                        isFinal = true
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = data.scheduleResponse.scheduleType.getButtonBackgroundColor(),
                        shape = RoundedCornerShape(16.dp)
                    ),
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
                            onClickScheduleInformation(
                                data.scheduleResponse.scheduleId,
                                data.scheduleResponse.scheduleType
                            )
                        }
                    }
                }
            )
        } else {
            Spacer(modifier = Modifier.height(18.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Brand200
            )
            Spacer(
                modifier = Modifier.height(18.dp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "공지",
                textAlign = TextAlign.Left,
                style = SubTitle2.copy(
                    lineHeight = 19.09.sp
                ),
                color = Gray900
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )

            if (data.scheduleResponse.notice.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEFE9F8),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            vertical = 22.dp,
                            horizontal = 20.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(88.dp),
                            painter = painterResource(id = com.mashup.core.common.R.drawable.img_placeholder_sleeping),
                            contentDescription = null
                        )
                        Text(
                            text = "공지가 없어요!",
                            style = Caption1,
                            color = Gray600
                        )
                    }
                }
            } else {
                Text(
                    text = data.scheduleResponse.notice,
                    maxLines = 5,
                    style = Body5.copy(
                        lineHeight = 20.sp
                    ),
                    color = Gray700,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = data.scheduleResponse.scheduleType.getButtonBackgroundColor(),
                            shape = RoundedCornerShape(16.dp)
                        ),
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
                                onClickScheduleInformation(
                                    data.scheduleResponse.scheduleId,
                                    data.scheduleResponse.scheduleType
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                    start,
                    end
                )
            }

            is UnderlineSpan -> addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline),
                start,
                end
            )

            is ForegroundColorSpan -> addStyle(
                SpanStyle(color = Color(span.foregroundColor)),
                start,
                end
            )
        }
    }
}
