package com.mashup.core.network.dao

import com.mashup.core.network.Response
import com.mashup.core.network.dto.PushHistoryResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PushHistoryDao {
    @GET("/api/v1/push-histories")
    suspend fun getPushHistory(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
    ): Response<PushHistoryResponse>

    @POST("/api/v1/push-histories/check")
    suspend fun getPushHistoryCheck(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
    ): Response<PushHistoryResponse>
}