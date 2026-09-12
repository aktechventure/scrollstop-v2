package com.aswinkumar.scrollstop.data.repository

import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl : SettingsRepository {

    private val _settings = MutableStateFlow(AppSettings())

    override fun getSettings(): Flow<AppSettings> = _settings.asStateFlow()

    override suspend fun updateDailyLimit(minutes: Int) {
        _settings.value = _settings.value.copy(dailyTimeLimitMinutes = minutes)
    }

    override suspend fun toggleNotification(enabled: Boolean) {
        _settings.value = _settings.value.copy(notificationsEnabled = enabled)
    }

    override suspend fun toggleDarkMode(enabled: Boolean) {
        _settings.value = _settings.value.copy(isDarkMode = enabled)
    }

    override suspend fun toggleFeed(packageName: String, enabled: Boolean) {
        val updatedFeeds = _settings.value.feedsList.map { feed ->
            if (feed.packageName == packageName) {
                feed.copy(isEnabled = enabled)
            } else {
                feed
            }
        }
        _settings.value = _settings.value.copy(feedsList = updatedFeeds)
    }

    override suspend fun clearAllUserData() {
        _settings.value = AppSettings()
    }
}
