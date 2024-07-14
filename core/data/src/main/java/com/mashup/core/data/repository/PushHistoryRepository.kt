package com.mashup.core.data.repository

import com.mashup.core.network.Response
import com.mashup.core.network.dao.PushHistoryDao
import com.mashup.core.network.dto.PushHistoryResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushHistoryRepository @Inject constructor(
    private val pushHistoryDao: PushHistoryDao
) {
    suspend fun getPushHistory(
        page: Int,
        size: Int,
        sort: String? = null
    ): Response<PushHistoryResponse> =
        pushHistoryDao.getPushHistory(page, size, sort)

    suspend fun getPushHistoryCheck(
        page: Int,
        size: Int,
        sort: String? = null
    ): Response<PushHistoryResponse> =
        pushHistoryDao.getPushHistoryCheck(page, size, sort)
}
