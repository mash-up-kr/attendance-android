package com.mashup.core.data.repository

import com.mashup.core.network.Response
import com.mashup.core.network.dao.MetaDao
import com.mashup.core.network.dto.RnbResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaRepository @Inject constructor(
    private val metaDao: MetaDao
) {
    suspend fun getRnb(): Response<RnbResponse> = metaDao.getRnb()
}
