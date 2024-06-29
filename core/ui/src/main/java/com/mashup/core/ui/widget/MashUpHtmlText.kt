package com.mashup.core.ui.widget

import android.text.Spanned
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 *  MashUpHtmlText.kt
 *
 *  Created by Minji Jeong on 2024/06/29
 *  Copyright © 2024 MashUp All rights reserved.
 */

@Composable
fun MashUpHtmlText(
    content: Spanned,
    modifier: Modifier = Modifier,
    @StyleRes textAppearance: Int? = null
) {
    AndroidView(
        factory = { context ->
            AppCompatTextView(
                context
            ).apply {
                if (textAppearance != null) {
                    setTextAppearance(textAppearance)
                }
                text = content
                includeFontPadding = false
            }
        },
        modifier = modifier,
        update = { view ->
            view.text = content
        },
    )
}
