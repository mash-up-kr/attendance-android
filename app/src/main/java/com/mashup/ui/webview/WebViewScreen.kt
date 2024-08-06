package com.mashup.ui.webview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun WebViewScreen(
    mashupBridge: MashupBridge,
    modifier: Modifier = Modifier,
    webViewUiState: WebViewUiState,
    isScrollTop: (Boolean) -> Unit = {},
    onBackPressed: () -> Unit = {},
    isShowMashUpToolbar : Boolean = true,
) {
    Column(modifier = modifier) {
        if(isShowMashUpToolbar){
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = (webViewUiState as? WebViewUiState.Success)?.title.orEmpty(),
                showBackButton = true,
                showBottomDivider = (webViewUiState as? WebViewUiState.Success)?.showToolbarDivider
                    ?: false,
                onClickBackButton = onBackPressed
            )
        }
        if (webViewUiState is WebViewUiState.Success) {
            MashUpWebView(
                webViewUrl = webViewUiState.webViewUrl,
                isScrollTop = isScrollTop,
                onBackPressed = onBackPressed,
                mashupBridge = mashupBridge
            )
        }
    }
}

@Composable
private fun MashUpWebView(
    webViewUrl: String?,
    mashupBridge: MashupBridge,
    isScrollTop: (Boolean) -> Unit = {},
    additionalHttpHeaders: Map<String, String> = emptyMap(),
    onBackPressed: () -> Unit
) {
    val webViewState = rememberWebViewState(
        url = webViewUrl.orEmpty(),
        additionalHttpHeaders = additionalHttpHeaders
    )
    val webViewNavigator = rememberWebViewNavigator()

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
                    addJavascriptInterface(mashupBridge, MashupBridge.name)
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
