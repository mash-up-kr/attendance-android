package com.mashup.core.network.dao

import com.mashup.core.network.Response
import com.mashup.core.network.dto.RnbResponse
import retrofit2.http.GET

interface MetaDao {
    @GET("/api/v1/meta/rnb")
    suspend fun getRnb(): Response<RnbResponse>
}
