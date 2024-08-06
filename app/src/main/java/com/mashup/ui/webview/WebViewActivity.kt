package com.mashup.ui.webview

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    private val viewModel: WebViewViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        setStatusBarColorRes(com.mashup.core.common.R.color.white)
        initWindowInset()
        initCompose()
    }

    private fun initCompose() {
        viewBinding.webView.setContent {
            val webViewUiState by viewModel.webViewUiState.collectAsState(WebViewUiState.Loading)
            val context = LocalContext.current

            WebViewScreen(
                modifier = Modifier.fillMaxSize(),
                webViewUiState = webViewUiState,
                onBackPressed = { finish() },
                isScrollTop = {
                    viewModel.onWebViewScroll(it)
                },
                mashupBridge = MashupBridge(context)
            )
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_web_view

    companion object {

        fun newIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
                putExtra(EXTRA_TITLE_KEY, title)
                putExtra(EXTRA_URL_KEY, url)
            }
        }
    }
}
