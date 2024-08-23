package com.mashup.ui.webview.birthday

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.common.utils.safeShow
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityBirthdayBinding
import com.mashup.ui.main.model.MainPopupType
import com.mashup.ui.main.popup.MainBottomPopup
import com.mashup.ui.webview.WebViewScreen
import com.mashup.ui.webview.WebViewUiState
import com.mashup.ui.webview.WebViewViewModel
import com.mashup.util.setFullScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BirthdayActivity : BaseActivity<ActivityBirthdayBinding>() {

    override val layoutId = R.layout.activity_birthday

    private val webViewViewModel by viewModels<WebViewViewModel>()

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            launch{
                webViewViewModel.showPopupType.collectLatest {
                    MainBottomPopup.newInstance(it).safeShow(supportFragmentManager)
                }
            }

            launch{
                webViewViewModel.onClickPopupConfirm.collectLatest { popupType ->
                    when (popupType) {
                        MainPopupType.BIRTHDAY_CELEBRATION -> {
                            webViewViewModel.disablePopup(popupType)
                            startActivity(
                                newIntent(
                                    context = this@BirthdayActivity,
                                    urlKey = "birthday/event"
                                )
                            )
                        }
                        else->{}
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MashUpTheme {
                val webViewUiState by webViewViewModel.webViewUiState.collectAsState(WebViewUiState.Loading)
                WebViewScreen(
                    modifier = Modifier.fillMaxSize().imePadding(),
                    webViewUiState = webViewUiState,
                    mashupBridge = MashupBridge(
                        this@BirthdayActivity,
                        onBackPressed = ::finish
                    ),
                    isShowMashUpToolbar = false
                )
            }
        }
        setFullScreen()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun newIntent(context: Context, urlKey: String = "birthday/crew-list"): Intent =
            Intent(context, BirthdayActivity::class.java).apply {
                putExtra(EXTRA_TITLE_KEY, "birthday")
                putExtra(EXTRA_URL_KEY, urlKey)
            }
    }
}
