package com.mashup.ui.webview.birthday

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.ui.webview.WebViewScreen
import com.mashup.ui.webview.WebViewUiState
import com.mashup.ui.webview.WebViewViewModel
import com.mashup.util.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BirthdayActivity : ComponentActivity() {

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
                        onBackPressed = ::finish
                    ),
                    isShowMashUpToolbar = false
                )
            }
        }
        setFullScreen()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, BirthdayActivity::class.java).apply {
            putExtra(EXTRA_TITLE_KEY, "birthday")
            putExtra(EXTRA_URL_KEY, "https://dev-app.mash-up.kr/mashong/")
        }
    }
}
