package com.mashup.ui.schedule

import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.extensions.month
import com.mashup.core.common.extensions.year
import com.mashup.data.dto.ScheduleResponse
import com.mashup.data.dto.SchedulesProgress
import com.mashup.data.repository.AttendanceRepository
import com.mashup.data.repository.ScheduleRepository
import com.mashup.datastore.data.repository.AppPreferenceRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.schedule.model.ScheduleCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val appPreferenceRepository: AppPreferenceRepository,
    private val scheduleRepository: ScheduleRepository,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel() {
    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Empty)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState

    private val _showCoachMark = MutableSharedFlow<Unit>()
    val showCoachMark: SharedFlow<Unit> = _showCoachMark

    fun getScheduleList() {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Loading)
            val generateNumber =
                userPreferenceRepository.getUserPreference().first().generationNumbers.last()

            scheduleRepository.getScheduleList(generateNumber)
                .onSuccess { response ->
                    _scheduleState.emit(
                        ScheduleState.Success(
                            scheduleTitleState = when {
                                response.progress == SchedulesProgress.DONE -> {
                                    ScheduleTitleState.End(generateNumber)
                                }
                                response.progress == SchedulesProgress.NOT_REGISTERED -> {
                                    ScheduleTitleState.Empty
                                }
                                response.progress == SchedulesProgress.ON_GOING &&
                                    response.dateCount != null -> {
                                    ScheduleTitleState.DateCount(response.dateCount)
                                }
                                else -> {
                                    ScheduleTitleState.SchedulePreparing
                                }
                            },
                            scheduleList = if (response.scheduleList.isEmpty()) {
                                listOf(ScheduleCard.EmptySchedule())
                            } else {
                                response.scheduleList.map { mapperToScheduleCard(it) }
                            },
                            monthlyScheduleList = getMonthlyScheduleList(response.scheduleList),
                            schedulePosition = getSchedulePosition(response.scheduleList)
                        )
                    )
                    showCoachMark(response.scheduleList)
                }.onFailure { code ->
                    handleErrorCode(code)
                    _scheduleState.emit(
                        ScheduleState.Success(
                            scheduleTitleState = ScheduleTitleState.Empty,
                            scheduleList = listOf(ScheduleCard.EmptySchedule()),
                            monthlyScheduleList = emptyList(),
                            schedulePosition = 0
                        )
                    )
                }
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Error(code))
        }
    }

    private suspend fun mapperToScheduleCard(scheduleResponse: ScheduleResponse): ScheduleCard {
        if (scheduleResponse.eventList.isEmpty()) {
            return ScheduleCard.EmptySchedule(scheduleResponse)
        }

        attendanceRepository.getScheduleAttendanceInfo(scheduleResponse.scheduleId)
            .onSuccess { response ->
                return if (response.attendanceInfos.isEmpty()) {
                    ScheduleCard.InProgressSchedule(
                        scheduleResponse = scheduleResponse,
                        attendanceInfo = response
                    )
                } else {
                    ScheduleCard.EndSchedule(
                        scheduleResponse = scheduleResponse,
                        attendanceInfo = response
                    )
                }
            }
            .onFailure {
                return ScheduleCard.EmptySchedule(scheduleResponse)
            }

        return ScheduleCard.EmptySchedule(scheduleResponse)
    }

    private fun getMonthlyScheduleList(scheduleList: List<ScheduleResponse>): List<Pair<String, List<ScheduleResponse>>> {
        return scheduleList.groupBy {
            val year = it.startedAt.year()
            val month = it.startedAt.month()
            "${year}년 ${month}월"
        }.toList()
    }

    private fun getSchedulePosition(schedules: List<ScheduleResponse>): Int {
        return schedules.size - schedules.filter { it.dateCount >= 0 }.size
    }

    private fun showCoachMark(schedules: List<ScheduleResponse>) {
        mashUpScope {
            val isShowCoachMark =
                appPreferenceRepository.getAppPreference().first().isShowCoachMarkInScheduleList
            val showCoachMark = schedules.any { it.dateCount >= 0 } && isShowCoachMark
            if (showCoachMark) {
                appPreferenceRepository.updateCoachMarkScheduleList(false)
                _showCoachMark.emit(Unit)
            }
        }
    }
}

sealed interface ScheduleTitleState {
    data class DateCount(val dataCount: Int) : ScheduleTitleState
    data class End(val generatedNumber: Int) : ScheduleTitleState
    object Empty : ScheduleTitleState
    object SchedulePreparing : ScheduleTitleState
}

sealed interface ScheduleState {
    object Empty : ScheduleState
    object Loading : ScheduleState
    data class Success(
        val scheduleTitleState: ScheduleTitleState,
        val scheduleList: List<ScheduleCard>,
        val monthlyScheduleList: List<Pair<String, List<ScheduleResponse>>>,
        val schedulePosition: Int
    ) : ScheduleState

    data class Error(val code: String) : ScheduleState
}
