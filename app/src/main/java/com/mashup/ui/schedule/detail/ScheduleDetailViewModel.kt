package com.mashup.ui.schedule.detail

import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.repository.ScheduleRepository
import com.mashup.ui.schedule.model.EventDetail
import com.mashup.ui.schedule.model.EventDetailMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val eventMapper: EventDetailMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val scheduleId = checkNotNull(savedStateHandle.get<Int>(EXTRA_SCHEDULE_ID))

    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Empty)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState

    init {
        getSchedule()
    }

    private fun getSchedule() {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Loading)
            scheduleRepository.getSchedule(scheduleId)
                .onSuccess { response ->
                    val eventList = eventMapper.getEventDetailList(
                        title = response.name,
                        date = response.getDate(),
                        eventList = response.eventList,
                        location = response.location
                    )

                    _scheduleState.emit(ScheduleState.Success(eventList))
                }
                .onFailure { code ->
                    handleErrorCode(code)
                }
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Error(code))
        }
    }
}

sealed interface ScheduleState {
    object Empty : ScheduleState
    object Loading : ScheduleState
    data class Success(
        val eventDetailList: List<EventDetail>
    ) : ScheduleState

    data class Error(val code: String) : ScheduleState
}
