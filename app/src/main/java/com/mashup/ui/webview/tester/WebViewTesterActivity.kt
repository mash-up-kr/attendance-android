package com.mashup.ui.webview.tester

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.feature.webview.WebViewScreen
import com.mashup.feature.webview.ui.theme.MashUpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewTesterActivity : ComponentActivity() {

    val webViewTesterViewModel: WebViewTesterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColorRes(com.mashup.core.common.R.color.white)

        setContent {
            MashUpTheme {
                val pageState by webViewTesterViewModel.testState.collectAsState()
                // A surface container using the 'background' color from the theme
                val webViewUrl by webViewTesterViewModel.webViewUrl.collectAsState()

                when (pageState) {
                    is PageState.Web -> {
                        WebViewScreen(
                            modifier = Modifier.fillMaxSize(),
                            webViewUrl = webViewUrl,
                            onBackPressed = {
                                webViewTesterViewModel.updateCurrentPageState(
                                    pageState = PageState.Selector
                                )
                            }
                        )
                    }
                    is PageState.Selector -> {
                        WebViewTestTypeScreen(
                            modifier = Modifier.fillMaxSize(),
                            onClickType = webViewTesterViewModel::onClickType
                        )
                    }
                }
            }
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
