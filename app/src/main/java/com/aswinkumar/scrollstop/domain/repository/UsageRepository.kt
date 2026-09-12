package com.aswinkumar.scrollstop.domain.repository

import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.model.UsageMetric
import kotlinx.coroutines.flow.Flow

interface UsageRepository {
    fun getTodayMetrics(): Flow<UsageMetric>
    fun getInterventionStats(period: TimePeriod): Flow<List<InterventionStat>>
    fun getTopTriggerApps(): Flow<List<AppTrigger>>
    suspend fun toggleAppMonitoring(packageName: String, isMonitored: Boolean)
}
