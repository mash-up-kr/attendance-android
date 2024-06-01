package com.mashup.core.ui.theme

import android.app.Activity
import android.graphics.Color
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.mashup.core.ui.colors.Brand100
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Brand600
import com.mashup.core.ui.colors.Gray50

private val LightColorPalette = lightColors(
    primary = Brand500,
    primaryVariant = Brand600,
    secondary = Brand100,
    onBackground = Gray50
)

@Composable
fun MashUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

@Composable
fun WebViewTheme(content: @Composable () -> Unit) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.TRANSPARENT
        }
    }
    MaterialTheme(
        colors = LightColorPalette,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
