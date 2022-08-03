package com.mashup.ui.attendance.platform

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.*
import com.mashup.compose.shape.CardListShape
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.*
import com.mashup.data.model.Platform
import com.mashup.data.model.PlatformInfo
import kotlin.math.max

@Composable
fun PlatformListItem(
    modifier: Modifier = Modifier,
    isAttendingEvent: Boolean = true,
    platformInfo: PlatformInfo,
    onClickPlatform: (PlatformInfo) -> Unit
) {
    val attendCount = remember(platformInfo) {
        max(
            0,
            platformInfo.totalCount - ((platformInfo.attendanceCount ?: 0) + (platformInfo.lateCount
                ?: 0))
        )
    }

    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 200.dp)
            .clickable {
                onClickPlatform(platformInfo)
            }
//            ambientColor 안되면 이거로...도전..
//            .drawColoredShadow(
//                color = colorResource(id = R.color.black),
//                offsetY = 20.dp,
//                offsetX = 20.dp
//            ),
            .shadow(
                elevation = 20.dp,
                shape = CardListShape,
                ambientColor = colorResource(id = R.color.black_10),
            ),
        shape = CardListShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp, horizontal = 12.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            PlatformInfo(
                platform = platformInfo.platform
            )

            Surface(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Gray100
                )
            }

            if (isAttendingEvent) {
                PlatformStatus(
                    numberOfAttend = platformInfo.attendanceCount ?: 0,
                    numberOfMaxAttend = platformInfo.totalCount
                )
            } else {
                PlatformAttendanceStatus(
                    modifier = Modifier
                        .padding(top = 14.dp),
                    numberOfAttend = platformInfo.attendanceCount ?: 0,
                    numberOfLateness = platformInfo.lateCount ?: 0,
                    numberOfAbsence = attendCount
                )
            }
        }
    }
}


fun Modifier.drawColoredShadow(
    color: Color,
    alpha: Float = 0.1f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = this.drawBehind {
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )
    }
}

@Composable
fun PlatformIcon(
    modifier: Modifier = Modifier,
    @DrawableRes platformImageRes: Int
) {
    Image(
        modifier = modifier
            .width(60.dp)
            .height(48.dp),
        painter = painterResource(id = platformImageRes),
        contentDescription = null
    )
}

@Composable
fun PlatformInfo(platform: String, modifier: Modifier = Modifier) {
    val platformImage = remember(platform) {
        when (platform) {
            Platform.DESIGN.name -> {
                R.drawable.img_statusprofile_design
            }
            Platform.ANDROID.name -> {
                R.drawable.img_statusprofile_android
            }
            Platform.WEB.name -> {
                R.drawable.img_statusprofile_web
            }
            Platform.IOS.name -> {
                R.drawable.img_statusprofile_ios
            }
            Platform.SPRING.name -> {
                R.drawable.img_statusprofile_spring
            }
            Platform.NODE.name -> {
                R.drawable.img_statusprofile_node
            }
            else -> {
                R.drawable.ic_img_placeholder_sleeping
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        PlatformIcon(platformImageRes = platformImage)

        MashTextView(
            modifier = Modifier.padding(top = 6.dp),
            text = platform,
            style = SubTitle1,
            color = Gray800
        )
    }
}

@Composable
fun PlatformStatus(
    modifier: Modifier = Modifier,
    numberOfAttend: Int = 0,
    numberOfMaxAttend: Int = 0,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        MashTextView(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(End),
            text = "출석/전체",
            style = Caption3,
            color = Gray500
        )

        Row(
            modifier = Modifier.padding(top = 2.dp),
            verticalAlignment = CenterVertically
        ) {
            MashTextView(
                text = "$numberOfAttend",
                style = Title3,
                color = if (numberOfAttend != 0) Green600 else Gray500
            )
            MashTextView(
                text = "/$numberOfMaxAttend",
                style = Title3,
                color = Gray700
            )
        }
    }
}

@Composable
fun PlatformAttendanceStatus(
    modifier: Modifier = Modifier,
    numberOfAttend: Int,
    numberOfLateness: Int,
    numberOfAbsence: Int
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = CenterVertically,
    ) {
        PlatformAttendanceStatusItem(
            label = "출석",
            value = numberOfAttend,
            valueColor = Green600
        )
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .padding(vertical = 9.dp),
            color = Gray100
        )
        PlatformAttendanceStatusItem(
            label = "지각",
            value = numberOfLateness,
            valueColor = Yellow600
        )
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .padding(vertical = 9.dp),
            color = Gray100
        )
        PlatformAttendanceStatusItem(
            label = "불참",
            value = numberOfAbsence,
            valueColor = Red600
        )
    }
}

@Composable
fun PlatformAttendanceStatusItem(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .defaultMinSize(minWidth = 43.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        MashTextView(
            text = label,
            color = Gray500,
            style = Caption3
        )
        MashTextView(
            modifier = Modifier.padding(2.dp),
            text = "$value",
            color = valueColor,
            style = SubTitle2
        )
    }
}

@Preview
@Composable
fun PlatformInfoPrev() {
    MashUpTheme {
        PlatformInfo(Platform.ANDROID.name)
    }
}

@Preview
@Composable
fun PlatformStatuePrev() {
    MashUpTheme {
        PlatformStatus(numberOfAttend = 13, numberOfMaxAttend = 20)
    }
}

@Preview
@Composable
fun PlatformListItemPrev() {
    MashUpTheme {
        PlatformListItem(
            modifier = Modifier.fillMaxWidth(),
            platformInfo = PlatformInfo(
                platform = Platform.ANDROID.name,
                totalCount = 13,
                attendanceCount = 0,
                lateCount = 7
            ),
            onClickPlatform = {}
        )
    }
}

@Preview(name = "출석이 끝난 리스트")
@Composable
fun EndedPlatformListItemPrev() {
    MashUpTheme {
        PlatformListItem(
            modifier = Modifier.fillMaxWidth(),
            isAttendingEvent = false,
            platformInfo = PlatformInfo(
                platform = Platform.ANDROID.name,
                totalCount = 13,
                attendanceCount = 0,
                lateCount = 7
            ),
            onClickPlatform = {}
        )
    }
}

@Preview
@Composable
fun PlatformAttendanceStatusItemPrev() {
    MashUpTheme {
        PlatformAttendanceStatusItem(
            modifier = Modifier.fillMaxWidth(),
            label = "출석",
            value = 10,
            valueColor = Green600
        )
    }
}

@Preview(widthDp = 200)
@Composable
fun PlatformAttendanceStatusWidth200Prev() {
    MashUpTheme {
        PlatformAttendanceStatus(
            modifier = Modifier.fillMaxWidth(),
            numberOfAttend = 10,
            numberOfLateness = 1,
            numberOfAbsence = 2
        )
    }
}

@Preview(widthDp = 400)
@Composable
fun PlatformAttendanceStatusWidth400Prev() {
    MashUpTheme {
        PlatformAttendanceStatus(
            modifier = Modifier.fillMaxWidth(),
            numberOfAttend = 10,
            numberOfLateness = 1,
            numberOfAbsence = 2
        )
    }
}

@Preview(widthDp = 800)
@Composable
fun PlatformAttendanceStatusWidth800Prev() {
    MashUpTheme {
        PlatformAttendanceStatus(
            modifier = Modifier.fillMaxWidth(),
            numberOfAttend = 10,
            numberOfLateness = 1,
            numberOfAbsence = 2
        )
    }
}