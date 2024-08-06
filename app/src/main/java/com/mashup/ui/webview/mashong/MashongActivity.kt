package com.mashup.ui.webview.mashong

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.webview.WebViewScreen
import com.mashup.ui.webview.WebViewUiState
import com.mashup.ui.webview.WebViewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MashongActivity : ComponentActivity() {

    private val webViewViewModel by viewModels<WebViewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MashUpTheme {
                val webViewUiState by webViewViewModel.webViewUiState.collectAsState(WebViewUiState.Loading)
                WebViewScreen(
                    modifier = Modifier.fillMaxSize(),
                    webViewUiState = webViewUiState,
                    mashupBridge = MashupBridge(
                        this,
                        onBackPressed = ::finish,
                        onNavigateDanggn = {
                            startActivity(ShakeDanggnActivity.newIntent(this))
                        }
                    ),
                    isShowMashUpToolbar = false
                )

                BackHandler {
                    finish()
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
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}
