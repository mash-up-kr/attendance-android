package com.mashup.feature.webview

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@Composable
fun WebViewRoute(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    WebViewScreen(
        modifier = modifier,
        webViewUrl = "https://mash-up.kr/",
        onBackPressed = onBackPressed
    )
}

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    webViewUrl: String = "",
    onBackPressed: () -> Unit = {}
) {
    val webViewState = rememberWebViewState(url = webViewUrl)
    val webViewNavigator = rememberWebViewNavigator()
    WebView(
        modifier = modifier,
        state = webViewState,
        navigator = webViewNavigator,
        onCreated = { webView ->
            with(webView) {
                settings.run {
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    javaScriptEnabled = true
                    defaultTextEncodingName = "UTF-8"
                }
            }
        }
    )
    BackHandler(enabled = true) {
        onBackPressed()
    }
}
