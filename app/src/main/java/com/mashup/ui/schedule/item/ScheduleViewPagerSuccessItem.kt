package com.mashup.ui.schedule.item

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.mashup.R
import com.mashup.core.ui.colors.Brand100
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.typography.Body1
import com.mashup.data.dto.EventResponse
import com.mashup.ui.schedule.ViewEventTimeline
import com.mashup.ui.schedule.model.ScheduleCard
import com.mashup.ui.schedule.util.onBindAttendanceImage
import com.mashup.ui.schedule.util.onBindAttendanceStatus
import com.mashup.ui.schedule.util.onBindAttendanceTime

@Composable
fun ScheduleViewPagerSuccessItem(
    data: ScheduleCard.EndSchedule,
    modifier: Modifier = Modifier,
    onClickScheduleInformation: (Int) -> Unit = {},
    onClickAttendance: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = Gray100,
                shape = RoundedCornerShape(20.dp)
            ).clip(RoundedCornerShape(20.dp))
            .clickable {
                onClickScheduleInformation(data.scheduleResponse.scheduleId)
            }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardInfoItem(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            dDay = data.scheduleResponse.getDDay(),
            title = data.scheduleResponse.name,
            calendar = data.scheduleResponse.getDate(),
            timeLine = data.scheduleResponse.getTimeLine(),
            location = data.scheduleResponse.location?.detailAddress ?: ""
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(color = Gray50, shape = RoundedCornerShape(16.dp))
                .height(220.dp)
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
                    color = Brand100,
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
                        ResourcesCompat.getColor(
                            resources,
                            com.mashup.core.common.R.color.brand500,
                            null
                        )
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
