package com.example.moremenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moremenu.model.Menu.Companion.toMenu
import com.example.moremenu.model.MoreMenuSideEffect
import com.example.moremenu.model.MoreMenuState
import com.mashup.core.data.repository.MetaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MoreMenuViewModel @Inject constructor(
    private val metaRepository: MetaRepository
) : ViewModel() {

    private val _moreMenuState = MutableStateFlow(MoreMenuState())
    val moreMenuState = _moreMenuState.asStateFlow()

    private val _moreMenuEvent = MutableSharedFlow<MoreMenuSideEffect>()
    val moreMenuEvent = _moreMenuEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            metaRepository.getRnb().onSuccess {
                _moreMenuState.value = MoreMenuState(menus = it.toMenu())
            }
        }
    }

    fun onClickBackButton() {
        viewModelScope.launch {
            _moreMenuEvent.emit(MoreMenuSideEffect.NavigateBackStack)
        }
    }
}
