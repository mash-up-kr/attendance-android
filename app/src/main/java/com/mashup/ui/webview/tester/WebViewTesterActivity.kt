package com.mashup.ui.webview.tester

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.ui.webview.WebViewScreen
import com.mashup.ui.webview.WebViewUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewTesterActivity : ComponentActivity() {

    private val webViewTesterViewModel: WebViewTesterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            MashUpTheme {
                val webViewTestUiModel by webViewTesterViewModel.webViewTestState.collectAsState()

                val webViewUiState by remember(webViewTestUiModel.url) {
                    mutableStateOf(
                        WebViewUiState.Success(
                            title = "",
                            webViewUrl = webViewTestUiModel.url,
                            showToolbarDivider = true
                        )
                    )
                }

                when (webViewTestUiModel.page) {
                    is PageState.Web -> {
                        if (webViewTestUiModel.header.isNotEmpty()) {
                            WebViewScreen(
                                modifier = Modifier.fillMaxSize(),
                                webViewUiState = webViewUiState,
                                onBackPressed = {
                                    webViewTesterViewModel.updateCurrentPageState(
                                        pageState = PageState.Selector
                                    )
                                },
                                additionalHttpHeaders = webViewTestUiModel.header
                            )
                        }
                    }
                    is PageState.Selector -> {
                        WebViewTestTypeScreen(
                            modifier = Modifier.fillMaxSize(),
                            url = webViewTestUiModel.url,
                            updateUrl = webViewTesterViewModel::updateUrl,
                            onClickButton = {
                                webViewTesterViewModel.updateCurrentPageState(PageState.Web)
                            },
                            onClickBackButton = { finish() }
                        )
                    }
                }
            }
        }
        setFullScreen()
    }

    private fun ComponentActivity.setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                )
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(
                context,
                WebViewTesterActivity::class.java
            )
        }
    }
}
