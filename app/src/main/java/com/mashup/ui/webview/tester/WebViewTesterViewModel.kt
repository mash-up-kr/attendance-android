package com.mashup.ui.webview.tester

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class WebViewTesterViewModel : ViewModel() {

    private val _testState: MutableStateFlow<PageState> = MutableStateFlow(PageState.Selector)
    val testState = _testState.asStateFlow()

    private val _webViewUrl: MutableStateFlow<String> = MutableStateFlow("")
    val webViewUrl = _webViewUrl.asStateFlow()

    fun updateCurrentPageState(pageState: PageState) {
        _testState.update {
            pageState
        }
    }

    fun onClickType(typeUrl: TypeUrl) {
        _webViewUrl.update {
            typeUrl.url
        }
    }
}

sealed interface PageState {
    object Web : PageState
    object Selector : PageState
}
