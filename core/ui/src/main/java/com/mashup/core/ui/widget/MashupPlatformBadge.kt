package com.mashup.core.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.R
import com.mashup.core.ui.typography.Body3

@Composable
fun MashupPlatformBadge(
    platform: String,
    modifier: Modifier = Modifier,
) {
    val convertedPlatform = platform.convertCamelCase()


    Row(
        modifier = modifier
            .background(
                color = platform.getColor(),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(12.dp),
            painter = painterResource(id = platform.getIcon()),
            contentDescription = null,
        )
        Spacer(
            modifier = Modifier.width(4.dp)
        )
        Text(
            text = platform.convertCamelCase().name,
            style = Body3,
            color = Color.White
        )
    }
}


private fun String.getIcon(): Int {
    return when (this) {
        "ALL" -> R.drawable.ic_semina
        else -> R.drawable.ic_platform
    }

}

enum class PlatformType {
    Semina, Design, Spring, Ios, Android, Web, Node
}


private fun String.getColor(): Color {
    return when (this) {
        "ALL" -> Color(0xFF7CD5FF)
        else -> Color(0xFF8A61FF)
    }
}

private fun String.convertCamelCase(): PlatformType {
    return when (this) {
        "ALL" -> PlatformType.Semina
        "DESIGN" -> PlatformType.Design
        "SPRING" -> PlatformType.Spring
        "IOS" -> PlatformType.Ios
        "ANDROID" -> PlatformType.Android
        "WEB" -> PlatformType.Web
        "NODE" -> PlatformType.Node
        else -> PlatformType.Semina
    }
}


@Preview
@Composable
private fun PreviewMashUpPlatformBadge() {
    Column {
        MashupPlatformBadge(
            platform = "ALL"
        )
        MashupPlatformBadge(
            platform = "DESIGN"
        )
        MashupPlatformBadge(
            platform = "SPRING"
        )
        MashupPlatformBadge(
            platform = "IOS"
        )
        MashupPlatformBadge(
            platform = "ANDROID"
        )
        MashupPlatformBadge(
            platform = "WEB"
        )
        MashupPlatformBadge(
            platform = "NODE"
        )

    }

}