package com.mashup.ui.schedule

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mashup.R
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.typography.Caption2
import com.mashup.core.ui.typography.SubTitle3

@Composable
fun ViewEventTimeline(
    modifier: Modifier = Modifier,
    time: String = "",
    caption: String = "",
    @StringRes status: Int = R.string.attendance,
    @DrawableRes image: Int = com.mashup.core.common.R.drawable.ic_attendance_not_yet,
    isFinal: Boolean = false
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (divider, attendanceImage, attendanceCaption, attendanceTime, attendanceStatus) = createRefs()

        Image(
            modifier = Modifier
                .size(20.dp)
                .constrainAs(attendanceImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(attendanceStatus.bottom)
                },
            painter = painterResource(id = image),
            contentDescription = null
        )

        Text(
            modifier = Modifier.constrainAs(attendanceCaption) {
                top.linkTo(attendanceImage.top)
                bottom.linkTo(attendanceImage.bottom, margin = 3.dp)
                start.linkTo(attendanceImage.end, 8.dp)
            },
            text = caption,
            style = Body5,
            color = Gray600
        )

        Text(
            modifier = Modifier.constrainAs(attendanceStatus) {
                top.linkTo(attendanceCaption.top)
                bottom.linkTo(attendanceCaption.bottom)
                start.linkTo(attendanceCaption.end, 7.dp)
            },
            text = stringResource(status),
            style = SubTitle3.copy(
                fontWeight = FontWeight.W700
            ),
            color = Gray800
        )
        Text(
            modifier = Modifier.constrainAs(attendanceTime) {
                end.linkTo(parent.end)
                bottom.linkTo(attendanceStatus.bottom)
            },
            text = time,
            style = Caption2,
            color = Gray500
        )
        if (isFinal.not()) {
            Row(
                modifier = Modifier.constrainAs(divider) {
                    start.linkTo(attendanceImage.start)
                    end.linkTo(attendanceImage.end)
                    top.linkTo(attendanceStatus.bottom, 6.dp)
                    height = Dimension.value(16.dp)
                    width = Dimension.value(1.dp)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.height(12.dp),
                    thickness = 1.dp,
                    color = Gray200
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewViewEventTimeline() {
    MashUpTheme {
        ViewEventTimeline()
    }
}
