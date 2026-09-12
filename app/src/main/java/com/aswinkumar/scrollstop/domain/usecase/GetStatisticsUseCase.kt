package com.aswinkumar.scrollstop.domain.usecase

import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow

class GetStatisticsUseCase(
    private val repository: UsageRepository
) {
    fun getStats(period: TimePeriod): Flow<List<InterventionStat>> =
        repository.getInterventionStats(period)

    fun getTopTriggers(): Flow<List<AppTrigger>> =
        repository.getTopTriggerApps()
}
