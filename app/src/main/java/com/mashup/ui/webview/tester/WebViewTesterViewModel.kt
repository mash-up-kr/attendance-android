package com.mashup.ui.webview.tester

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.datastore.data.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
@HiltViewModel
class WebViewTesterViewModel @Inject constructor(
    userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val testState: MutableStateFlow<PageState> = MutableStateFlow(PageState.Selector)

    private val webViewUrl: MutableStateFlow<String> = MutableStateFlow("")

    private val accessToken: StateFlow<String> = userPreferenceRepository.getUserPreference().map { userPref ->
        userPref.token
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val webViewTestState = combine(
        testState,
        webViewUrl,
        accessToken
    ) { page, url, token ->
        WebViewTestUiModel(
            page = page,
            url = url,
            header = mapOf(Pair("authorization", token))
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        WebViewTestUiModel()

    )

    fun updateCurrentPageState(pageState: PageState) {
        testState.update {
            pageState
        }
    }

    fun updateUrl(url: String) {
        webViewUrl.update {
            url
        }
    }
}

data class WebViewTestUiModel(
    val page: PageState = PageState.Selector,
    val url: String = "",
    val header: Map<String, String> = emptyMap()
)
sealed interface PageState {
    object Web : PageState
    object Selector : PageState
}
