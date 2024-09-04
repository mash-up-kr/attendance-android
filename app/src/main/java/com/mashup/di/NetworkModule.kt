package com.mashup.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.mashup.BuildConfig.DEBUG_MODE
import com.mashup.core.model.Platform
import com.mashup.core.network.adapter.PlatformJsonAdapter
import com.mashup.core.network.dao.MetaDao
import com.mashup.core.network.dao.PopupDao
import com.mashup.core.network.dao.PushHistoryDao
import com.mashup.core.network.dao.StorageDao
import com.mashup.data.network.API_HOST
import com.mashup.network.CustomDateAdapter
import com.mashup.network.dao.AttendanceDao
import com.mashup.network.dao.MemberDao
import com.mashup.network.dao.MemberProfileDao
import com.mashup.network.dao.ScheduleDao
import com.mashup.network.dao.ScoreDao
import com.mashup.network.interceptor.AuthInterceptor
import com.mashup.network.interceptor.BaseInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    companion object {
        val flipperNetwork = NetworkFlipperPlugin()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, CustomDateAdapter().nullSafe())
        .add(Platform::class.java, PlatformJsonAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideFlipperOkhttpInterceptor() =
        FlipperOkhttpInterceptor(flipperNetwork)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        baseInterceptor: BaseInterceptor,
        flipperInterceptor: FlipperOkhttpInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(baseInterceptor)

        if (DEBUG_MODE) {
            okHttpClient
                .addNetworkInterceptor(flipperInterceptor)
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
        }
        return okHttpClient
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .callTimeout(10L, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_HOST)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideMemberDao(
        retrofit: Retrofit
    ): MemberDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideAttendanceDao(
        retrofit: Retrofit
    ): AttendanceDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideScheduleDao(
        retrofit: Retrofit
    ): ScheduleDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideScoreDao(
        retrofit: Retrofit
    ): ScoreDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideStorageDao(
        retrofit: Retrofit
    ): StorageDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun providePopupDao(
        retrofit: Retrofit
    ): PopupDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideMemberProfileDao(
        retrofit: Retrofit
    ): MemberProfileDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideMetaDao(
        retrofit: Retrofit
    ): MetaDao {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun providePushHistoryDao(
        retrofit: Retrofit
    ): PushHistoryDao {
        return retrofit.create()
    }
}
