package com.aswinkumar.scrollstop.data.repository

import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.model.UsageMetric
import com.aswinkumar.scrollstop.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsageRepositoryImpl : UsageRepository {

    private val _todayMetric = MutableStateFlow(
        UsageMetric(
            timeSavedMinutes = 42,
            interventionsCount = 14,
            totalScreenTimeMinutes = 108,
            todayFocusGoal = "Deep Reading & Mindfulness",
            focusGoalProgress = 0.72f
        )
    )

    private val _appTriggers = MutableStateFlow(
        listOf(
            AppTrigger("1", "Instagram", "com.instagram.android", 8, 45, true),
            AppTrigger("2", "YouTube", "com.google.android.youtube", 4, 35, true),
            AppTrigger("3", "TikTok", "com.zhiliaoapp.musically", 2, 18, true),
            AppTrigger("4", "X / Twitter", "com.twitter.android", 0, 10, false)
        )
    )

    override fun getTodayMetrics(): Flow<UsageMetric> = _todayMetric.asStateFlow()

    override fun getInterventionStats(period: TimePeriod): Flow<List<InterventionStat>> {
        val stats = when (period) {
            TimePeriod.DAY -> listOf(
                InterventionStat("8 AM", 1, 5),
                InterventionStat("11 AM", 3, 12),
                InterventionStat("2 PM", 5, 15),
                InterventionStat("5 PM", 3, 8),
                InterventionStat("8 PM", 2, 2)
            )
            TimePeriod.WEEK -> listOf(
                InterventionStat("Mon", 12, 35),
                InterventionStat("Tue", 18, 50),
                InterventionStat("Wed", 14, 42),
                InterventionStat("Thu", 9, 28),
                InterventionStat("Fri", 15, 45),
                InterventionStat("Sat", 22, 65),
                InterventionStat("Sun", 10, 30)
            )
            TimePeriod.MONTH -> listOf(
                InterventionStat("Week 1", 85, 240),
                InterventionStat("Week 2", 92, 280),
                InterventionStat("Week 3", 78, 220),
                InterventionStat("Week 4", 105, 310)
            )
        }
        return MutableStateFlow(stats).asStateFlow()
    }

    override fun getTopTriggerApps(): Flow<List<AppTrigger>> = _appTriggers.asStateFlow()

    override suspend fun toggleAppMonitoring(packageName: String, isMonitored: Boolean) {
        _appTriggers.value = _appTriggers.value.map { item ->
            if (item.packageName == packageName) {
                item.copy(isMonitored = isMonitored)
            } else {
                item
            }
        }
    }
}
