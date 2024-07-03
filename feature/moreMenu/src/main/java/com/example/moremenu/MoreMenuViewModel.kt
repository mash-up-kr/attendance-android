package com.example.moremenu

import androidx.lifecycle.ViewModel
import com.example.moremenu.model.MoreMenuSideEffect
import com.example.moremenu.model.MoreMenuState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoreMenuViewModel @Inject constructor() : ViewModel() {

    private val _moreMenuState = MutableStateFlow(MoreMenuState())
    val moreMenuState = _moreMenuState.asStateFlow()

    private val _moreMenuEvent = MutableSharedFlow<MoreMenuSideEffect>()
    val moreMenuEvent = _moreMenuEvent.asSharedFlow()
}
