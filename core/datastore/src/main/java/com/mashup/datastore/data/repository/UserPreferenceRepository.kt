package com.mashup.datastore.data.repository

import androidx.datastore.core.DataStore
import com.mashup.core.model.Platform
import com.mashup.core.model.data.local.UserPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceRepository @Inject constructor(
    private val userPreferenceDataSource: DataStore<UserPreference>
) {
    fun getUserPreference() = userPreferenceDataSource.data

    suspend fun updateUserPreference(
        token: String = "",
        name: String,
        platform: Platform,
        generationNumbers: List<Int>,
        pushNotificationAgreed: Boolean
    ) {
        userPreferenceDataSource.updateData { savedUserPreferences ->
            savedUserPreferences.copy(
                token = token.ifBlank { savedUserPreferences.token },
                name = name,
                platform = platform,
                generationNumbers = generationNumbers,
                pushNotificationAgreed = pushNotificationAgreed
            )
        }
    }

    suspend fun updateUserToken(
        token: String
    ) {
        userPreferenceDataSource.updateData { savedUserPreferences ->
            savedUserPreferences.copy(
                token = token
            )
        }
    }

    suspend fun updateUserPushNotificationAgreed(
        pushNotificationAgreed: Boolean
    ) {
        userPreferenceDataSource.updateData { savedUserPreferences ->
            savedUserPreferences.copy(
                pushNotificationAgreed = pushNotificationAgreed
            )
        }
    }

    suspend fun clearUserPreference() {
        userPreferenceDataSource.updateData {
            UserPreference.getDefaultInstance()
        }
    }
}