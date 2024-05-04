package com.mashup.ui.webview.tester

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WebViewTestTypeScreen(
    modifier: Modifier = Modifier,
    onClickType: (TypeUrl) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TypeUrl.typeUrlList.forEach { type ->
            key(
                type.title
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onClickType(type)
                    },
                    text = type.title
                )
            }
        }
    }
}

sealed interface TypeUrl {
    val url: String
    val title: String
    data class MashUpKr(
        override val title: String = "Mash-Up KR",
        override val url: String = "https://mash-up.kr"
    ) : TypeUrl

    data class Recruit(
        override val title: String = "Mash-Up Recruit",
        override val url: String = "https://recruit.mash-up.kr/"
    ) : TypeUrl

    data class AdminSoo(
        override val title: String = "Mash-Up AdminSoo",
        override val url: String = "https://adminsoo.mash-up.kr"
    ) : TypeUrl

    companion object {
        val typeUrlList = listOf(
            MashUpKr(),
            Recruit(),
            AdminSoo()
        )
    }
}
