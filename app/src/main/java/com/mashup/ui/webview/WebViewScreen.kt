package com.mashup.ui.webview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.mashup.ui.webview.tester.MashupBridge

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    webViewUiState: WebViewUiState,
    isScrollTop: (Boolean) -> Unit = {},
    additionalHttpHeaders: Map<String, String> = emptyMap(),
    onBackPressed: () -> Unit
) {
    Column(modifier = modifier) {
        if (webViewUiState is WebViewUiState.Success) {
            MashUpWebView(
                webViewUrl = webViewUiState.webViewUrl,
                isScrollTop = isScrollTop,
                onBackPressed = onBackPressed,
                additionalHttpHeaders = additionalHttpHeaders
            )
        }
    }
}

@Composable
private fun MashUpWebView(
    webViewUrl: String?,
    isScrollTop: (Boolean) -> Unit = {},
    additionalHttpHeaders: Map<String, String> = emptyMap(),
    onBackPressed: () -> Unit = {}
) {
    val webViewState = rememberWebViewState(
        url = webViewUrl.orEmpty(),
        additionalHttpHeaders = additionalHttpHeaders
    )
    val webViewNavigator = rememberWebViewNavigator()
    val context = LocalContext.current

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        onCreated = { webView ->
            with(webView) {
                settings.run {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    defaultTextEncodingName = "UTF-8"
                    addJavascriptInterface(MashupBridge(context), "MashupBridge")
                }
                setOnScrollChangeListener { view, _, _, _, _ ->
                    isScrollTop(!view.canScrollVertically(-1))
                }
            }
        }
    )

    BackHandler(enabled = true) {
        onBackPressed()
    }
}
