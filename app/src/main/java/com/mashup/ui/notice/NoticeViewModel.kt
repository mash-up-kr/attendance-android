package com.mashup.ui.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notice.model.NoticeSideEffect
import com.example.notice.model.NoticeState
import com.mashup.core.data.repository.PushHistoryRepository
import com.mashup.core.network.Response
import com.mashup.core.network.dto.PushHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val pushHistoryRepository: PushHistoryRepository
) : ViewModel() {

    private val _noticeState = MutableStateFlow(NoticeState())
    val noticeState = _noticeState.asStateFlow()

    private val _noticeEvent = MutableSharedFlow<NoticeSideEffect>()
    val noticeEvent = _noticeEvent.asSharedFlow()

    private var _currentPage = MutableStateFlow(1)

    init {
        viewModelScope.launch {
            val noticeResponse: Response<PushHistoryResponse> =
                pushHistoryRepository.getPushHistory(
                    page = _currentPage.value,
                    size = PAGE_SIZE,
                    sort = DESC
                )

            if (noticeResponse.isSuccess()) {
                val pushHistoryResponse = noticeResponse.data
                onReadNewNoticeList()
                _noticeState.value = NoticeState(
                    oldNoticeList = pushHistoryResponse?.read ?: emptyList(),
                    newNoticeList = pushHistoryResponse?.unread ?: emptyList(),
                    isError = false
                )
            } else {
                _noticeState.value = NoticeState(
                    isError = true
                )
            }
        }
    }

    fun onLoadNextNotice() {
        viewModelScope.launch {
            _currentPage.value += 1
            val noticeResponse: Response<PushHistoryResponse> =
                pushHistoryRepository.getPushHistory(
                    page = _currentPage.value,
                    size = PAGE_SIZE,
                    sort = DESC
                )

            if (noticeResponse.isSuccess()) {
                val pushHistoryResponse = noticeResponse.data
                onReadNewNoticeList()
                _noticeState.value = NoticeState(
                    oldNoticeList = _noticeState.value.oldNoticeList + pushHistoryResponse?.read.orEmpty(),
                    newNoticeList = _noticeState.value.newNoticeList + pushHistoryResponse?.unread.orEmpty(),
                    isError = false
                )
            } else {
                _noticeState.value = NoticeState(
                    isError = true
                )
            }
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            _noticeEvent.emit(NoticeSideEffect.OnBackPressed)
        }
    }

    fun onClickNoticeItem(notice: PushHistoryResponse.Notice) {
        viewModelScope.launch {
            _noticeEvent.emit(NoticeSideEffect.OnNavigateMenu(notice))
        }
    }

    private fun onReadNewNoticeList() {
        viewModelScope.launch {
            val currentPage = _currentPage.value
            pushHistoryRepository.postPushHistoryCheck(
                page = currentPage,
                size = PAGE_SIZE,
                sort = DESC
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 100
        const val DESC = "createdAt,desc"
    }
}
