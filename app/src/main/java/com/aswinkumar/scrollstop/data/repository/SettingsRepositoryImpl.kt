package com.aswinkumar.scrollstop.data.repository

import com.aswinkumar.scrollstop.data.local.ScrollStopDatabase
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity
import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val database: ScrollStopDatabase
) : SettingsRepository {

    override fun getSettings(): Flow<AppSettings> =
        database.userPreferenceDao().getPreference().map { preference ->
            preference?.toSettings() ?: AppSettings()
        }

    override suspend fun updateDailyLimit(minutes: Int) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        database.userPreferenceDao().insertOrUpdate(current.copy(dailyTimeLimitMinutes = minutes))
    }

    override suspend fun toggleNotification(enabled: Boolean) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        database.userPreferenceDao().insertOrUpdate(current.copy(notificationsEnabled = enabled))
    }

    override suspend fun toggleDarkMode(enabled: Boolean) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        database.userPreferenceDao().insertOrUpdate(current.copy(themePreference = if (enabled) "DARK" else "LIGHT"))
    }

    override suspend fun toggleFeed(packageName: String, enabled: Boolean) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        val packages = current.targetedApps.split(",").filter { it.isNotBlank() }.toMutableSet()
        if (enabled) packages.add(packageName) else packages.remove(packageName)
        database.userPreferenceDao().insertOrUpdate(current.copy(targetedApps = packages.joinToString(",")))
    }

    suspend fun updateQuietHours(start: String, end: String) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        database.userPreferenceDao().insertOrUpdate(current.copy(quietHoursStart = start, quietHoursEnd = end))
    }

    suspend fun updateInterventionThreshold(minutes: Int) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        database.userPreferenceDao().insertOrUpdate(current.copy(interventionThresholdMinutes = minutes))
    }

    suspend fun togglePrivacyMode(enabled: Boolean) {
        val current = database.userPreferenceDao().getPreferenceSync() ?: UserPreferenceEntity()
        database.userPreferenceDao().insertOrUpdate(current.copy(privacyModeEnabled = enabled))
    }

    override suspend fun clearAllUserData() {
        database.deleteAllUserData()
    }

    private fun UserPreferenceEntity.toSettings(): AppSettings {
        val enabledPackages = targetedApps.split(",").filter { it.isNotBlank() }.toSet()
        return AppSettings(
            dailyTimeLimitMinutes = dailyTimeLimitMinutes,
            notificationsEnabled = notificationsEnabled,
            quietHoursStart = quietHoursStart,
            quietHoursEnd = quietHoursEnd,
            interventionThresholdMinutes = interventionThresholdMinutes,
            privacyModeEnabled = privacyModeEnabled,
            isDarkMode = themePreference == "DARK",
            feedsList = AppSettings().feedsList.map { feed ->
                feed.copy(isEnabled = feed.packageName in enabledPackages)
            }
        )
    }
}
