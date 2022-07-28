package com.mashup.data.repository

import com.mashup.common.Response
import com.mashup.data.dto.ScheduleResponse
import com.mashup.network.dao.ScheduleDao
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val scheduleDao: ScheduleDao
) {
    suspend fun getSchedule(scheduleId: Int): Response<ScheduleResponse> {
        return scheduleDao.getSchedule(
            id = scheduleId
        )
    }

    suspend fun getScheduleList(generationNumber: Int): Response<List<ScheduleResponse>> {
        return scheduleDao.getScheduleList(
            generationNumber = generationNumber
        )
    }
}