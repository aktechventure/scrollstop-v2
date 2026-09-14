package com.aswinkumar.scrollstop.domain.repository

import com.aswinkumar.scrollstop.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun updateDailyLimit(minutes: Int)
    suspend fun toggleNotification(enabled: Boolean)
    suspend fun toggleDarkMode(enabled: Boolean)
    suspend fun toggleFeed(packageName: String, enabled: Boolean)
    suspend fun clearAllUserData()
    suspend fun updateQuietHours(start: String, end: String)
    suspend fun updateInterventionThreshold(minutes: Int)
    suspend fun togglePrivacyMode(enabled: Boolean)
}
