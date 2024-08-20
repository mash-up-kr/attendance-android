package com.mashup.ui.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.data.repository.PopUpRepository
import com.mashup.data.network.WEB_HOST
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.main.model.MainPopupType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    userPreferenceRepository: UserPreferenceRepository,
    private val popUpRepository: PopUpRepository,
) : BaseViewModel() {

    private val showDividerFlow = MutableStateFlow(false)
    private val _showPopupType = MutableSharedFlow<MainPopupType>()
    val showPopupType: SharedFlow<MainPopupType> = _showPopupType.asSharedFlow()

    private val _onClickPopupConfirm = MutableSharedFlow<MainPopupType>()
    val onClickPopupConfirm: SharedFlow<MainPopupType> = _onClickPopupConfirm.asSharedFlow()

    init {
        getBirthDayPopup()
    }

    val webViewUiState = combine(
        savedStateHandle.getStateFlow(EXTRA_TITLE_KEY, ""),
        savedStateHandle.getStateFlow(EXTRA_URL_KEY, ""),
        showDividerFlow,
        userPreferenceRepository.getUserPreference()
    ) { title, webViewUrl, showDivider, prefs ->
        var convertWebViewUrl = WEB_HOST + webViewUrl
        if (title == "mashong") {
            convertWebViewUrl += prefs.platform
        }
        WebViewUiState.Success(
            title = title,
            webViewUrl = convertWebViewUrl,
            showToolbarDivider = showDivider,
            additionalHttpHeaders = mapOf(Pair("authorization", prefs.token))
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        WebViewUiState.Loading
    )

    fun onWebViewScroll(isScrollTop: Boolean) = mashUpScope {
        showDividerFlow.emit(isScrollTop.not())
    }

    override fun handleErrorCode(code: String) {
    }

    private fun getBirthDayPopup() = mashUpScope {
        val result = popUpRepository.getPopupKeyList()
        val url = savedStateHandle.getStateFlow(EXTRA_URL_KEY, "").value

        if(url == "birthday/event") {
           return@mashUpScope
        }


        if(result.isSuccess()){
            val popupType =
                MainPopupType.getMainPopupType(result.data?.firstOrNull() ?: MainPopupType.BIRTHDAY_CELEBRATION.name)
            if (popupType != MainPopupType.BIRTHDAY_CELEBRATION) return@mashUpScope
            _showPopupType.emit(popupType)
        }
    }

    fun disablePopup(popupKey: MainPopupType) = mashUpScope {
        if (popupKey == MainPopupType.UNKNOWN) return@mashUpScope
        popUpRepository.patchPopupDisabled(popupKey.name)
    }


    fun onClickPopup(popupKey: String) = mashUpScope {
        val popupType =
            MainPopupType.getMainPopupType(popupKey)
        if (popupType == MainPopupType.UNKNOWN) return@mashUpScope
        _onClickPopupConfirm.emit(popupType)
    }
}

sealed interface WebViewUiState {
    object Loading : WebViewUiState

    data class Success(
        val title: String,
        val webViewUrl: String,
        val showToolbarDivider: Boolean,
        val additionalHttpHeaders: Map<String, String>
    ) : WebViewUiState
}
