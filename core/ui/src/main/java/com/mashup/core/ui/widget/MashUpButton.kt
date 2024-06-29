package com.mashup.core.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand100
import com.mashup.core.ui.colors.Brand300
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body1

enum class ButtonStyle(
    val backgroundColor: Color,
    val textColor: Color
) {
    PRIMARY(backgroundColor = Brand500, textColor = White),
    INVERSE(backgroundColor = Brand100, textColor = Brand500),
    DISABLE(backgroundColor = Brand300, textColor = White),
    DARK(backgroundColor = Gray800, textColor = White),
    DEFAULT(backgroundColor = Gray100, textColor = Gray600)
}

@Composable
fun MashUpButton(
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle = ButtonStyle.PRIMARY,
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    showLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .height(52.dp)
            .background(if (isEnabled) buttonStyle.backgroundColor else ButtonStyle.DISABLE.backgroundColor)
            .padding(horizontal = 20.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                enabled = isEnabled || showLoading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = showLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ButtonCircularProgressbar(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp)
                )
            }

            Text(
                text = text,
                style = Body1.copy(
                    color = buttonStyle.textColor
                )
            )
        }
    }
}

@Composable
fun ButtonCircularProgressbar(
    modifier: Modifier = Modifier,
    progressBarWidth: Dp = 3.dp,
    progressBarColor: Color = Color(0xFFFFFFFF),
    backgroundProgressBarColor: Color = Color(0x80FFFFFF),
    progressDuration: Int = 500
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation =
            keyframes {
                durationMillis = progressDuration
            }
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2 - progressBarWidth.toPx() / 2

        // ProgressBar Background(Circle)
        drawCircle(
            color = backgroundProgressBarColor,
            radius = radius,
            style = Stroke(width = progressBarWidth.toPx())
        )

        // ProgressBar(Arc)
        drawArc(
            color = progressBarColor,
            startAngle = angle,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = size.center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = progressBarWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

@Preview(backgroundColor = 0xFF6A36FF, showBackground = true)
@Composable
fun PrevButtonCircularProgressbar() {
    MashUpTheme {
        Box {
            ButtonCircularProgressbar(
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(widthDp = 320, backgroundColor = 0xFFF8F7FC, showBackground = true)
@Composable
fun PrevMashUpButton() {
    MashUpTheme {
        Column {
            MashUpButton(
                modifier = Modifier.padding(16.dp),
                buttonStyle = ButtonStyle.PRIMARY,
                text = "다음",
                onClick = {},
                showLoading = true
            )

            MashUpButton(
                modifier = Modifier.padding(16.dp),
                buttonStyle = ButtonStyle.PRIMARY,
                text = "다음",
                onClick = {}
            )

            MashUpButton(
                modifier = Modifier.padding(16.dp),
                buttonStyle = ButtonStyle.DEFAULT,
                text = "다음",
                onClick = {}
            )

            MashUpButton(
                modifier = Modifier.padding(16.dp),
                buttonStyle = ButtonStyle.PRIMARY,
                text = "다음",
                onClick = {},
                isEnabled = false
            )
        }
    }
}
