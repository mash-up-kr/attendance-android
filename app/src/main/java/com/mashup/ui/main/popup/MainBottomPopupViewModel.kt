package com.mashup.ui.main.popup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_POPUP_KEY
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.data.repository.PopUpRepository
import com.mashup.core.data.repository.StorageRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.main.model.MainPopupEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainBottomPopupViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val popUpRepository: PopUpRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val popupKey = savedStateHandle.get<String>(EXTRA_POPUP_KEY)

    private val _uiState = mutableStateOf<MainBottomPopupUiState>(MainBottomPopupUiState.Loading)
    val uiState: State<MainBottomPopupUiState> = _uiState

    init {
        getStorage()
    }

    private fun getStorage() = mashUpScope {
        if (popupKey.isNullOrBlank()) return@mashUpScope
        val result = storageRepository.getStorage(popupKey)


        val userName = userPreferenceRepository.getUserPreference().first().name
        val title = result.data?.valueMap?.get("title").orEmpty().replace("\${name}", userName)

        if (result.isSuccess()) {
            _uiState.value = MainBottomPopupUiState.Success(
                MainPopupEntity(
                    title = title,
                    description = result.data?.valueMap?.get("subtitle").orEmpty(),
                    imageResName = result.data?.valueMap?.get("imageName").orEmpty(),
                    leftButtonText = result.data?.valueMap?.get("leftButtonTitle").orEmpty(),
                    rightButtonText = result.data?.valueMap?.get("rightButtonTitle").orEmpty(),
                )
            )
        }
    }

    fun patchPopupViewed() = mashUpScope {
        if (popupKey.isNullOrBlank()) return@mashUpScope
        popUpRepository.patchPopupViewed(popupKey)
    }
}

sealed interface MainBottomPopupUiState {
    object Loading : MainBottomPopupUiState

    data class Success(
        val mainPopupEntity: MainPopupEntity
    ) : MainBottomPopupUiState
}
