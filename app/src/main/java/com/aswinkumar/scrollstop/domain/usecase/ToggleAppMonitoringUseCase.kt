package com.aswinkumar.scrollstop.domain.usecase

import com.aswinkumar.scrollstop.domain.repository.UsageRepository

class ToggleAppMonitoringUseCase(
    private val repository: UsageRepository
) {
    suspend operator fun invoke(packageName: String, enabled: Boolean) =
        repository.toggleAppMonitoring(packageName, enabled)
}
