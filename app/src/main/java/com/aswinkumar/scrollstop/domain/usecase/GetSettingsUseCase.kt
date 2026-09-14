package com.aswinkumar.scrollstop.domain.usecase

import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> = repository.getSettings()

    suspend fun updateLimit(minutes: Int) = repository.updateDailyLimit(minutes)
    suspend fun toggleFeed(packageName: String, enabled: Boolean) = repository.toggleFeed(packageName, enabled)
    suspend fun updateQuietHours(start: String, end: String) = repository.updateQuietHours(start, end)
    suspend fun updateInterventionThreshold(minutes: Int) = repository.updateInterventionThreshold(minutes)
    suspend fun togglePrivacyMode(enabled: Boolean) = repository.togglePrivacyMode(enabled)
    suspend fun clearData() = repository.clearAllUserData()
}
