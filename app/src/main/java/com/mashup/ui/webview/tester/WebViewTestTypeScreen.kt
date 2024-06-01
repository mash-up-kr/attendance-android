package com.mashup.ui.webview.tester

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun WebViewTestTypeScreen(
    modifier: Modifier = Modifier,
    url: String = "",
    updateUrl: (String) -> Unit = {},
    onClickButton: () -> Unit = {},
    onClickBackButton: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MashUpToolbar(
            title = "웹뷰 Test",
            showBackButton = true,
            onClickBackButton = onClickBackButton
        )
        TextField(value = url, onValueChange = updateUrl)
        Button(
            onClick = onClickButton
        ) {
            Text(
                text = "웹뷰 열기"
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWebViewTestTypeScreen() {
    MashUpTheme {
        var url by remember { mutableStateOf("") }
        WebViewTestTypeScreen(
            modifier = Modifier.fillMaxSize(),
            url = url,
            updateUrl = {
                url = it
            }

        )
    }
}
