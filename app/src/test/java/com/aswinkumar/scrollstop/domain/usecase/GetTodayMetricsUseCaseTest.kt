package com.aswinkumar.scrollstop.domain.usecase

import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.model.UsageMetric
import com.aswinkumar.scrollstop.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTodayMetricsUseCaseTest {
    @Test
    fun emitsRepositoryMetricWithoutTransformingIt() = runBlocking {
        val expected = UsageMetric(4, 2, 30, "Focus", 0.5f)
        val useCase = GetTodayMetricsUseCase(object : UsageRepository {
            override fun getTodayMetrics(): Flow<UsageMetric> = flowOf(expected)
            override fun getInterventionStats(period: TimePeriod): Flow<List<InterventionStat>> =
                flowOf(emptyList())
            override fun getTopTriggerApps(): Flow<List<AppTrigger>> = flowOf(emptyList())
            override suspend fun toggleAppMonitoring(packageName: String, isMonitored: Boolean) = Unit
        })

        assertEquals(expected, useCase().first())
    }
}
