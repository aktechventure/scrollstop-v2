package com.aswinkumar.scrollstop.data.repository

import com.aswinkumar.scrollstop.data.local.dao.AppUsageDao
import com.aswinkumar.scrollstop.data.local.dao.InterventionSessionDao
import com.aswinkumar.scrollstop.platform.UsageStatsDataSource
import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.model.UsageMetric
import com.aswinkumar.scrollstop.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class UsageRepositoryImpl(
    private val appUsageDao: AppUsageDao,
    private val interventionSessionDao: InterventionSessionDao,
    private val usageStatsDataSource: UsageStatsDataSource
) : UsageRepository {

    override fun getTodayMetrics(): Flow<UsageMetric> = flow {
        val today = usageStatsDataSource.refreshToday()
        emitAll(appUsageDao.getTodayUsage(today).map { usage ->
            val start = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val interventions = interventionSessionDao.getSessionsInTimeRangeSync(
                start,
                System.currentTimeMillis()
            )
            UsageMetric(
                timeSavedMinutes = interventions.sumOf {
                    ((it.endTime - it.startTime) / 60_000L).toInt()
                },
                interventionsCount = interventions.size,
                totalScreenTimeMinutes = (usage.sumOf { it.durationMillis } / 60_000L).toInt(),
                todayFocusGoal = "Deep Reading & Mindfulness",
                focusGoalProgress = 0f
            )
        })
    }

    override fun getInterventionStats(period: TimePeriod): Flow<List<InterventionStat>> {
        return flow {
            val now = System.currentTimeMillis()
            val duration = when (period) {
                TimePeriod.DAY -> 24L * 60 * 60 * 1000
                TimePeriod.WEEK -> 7L * 24 * 60 * 60 * 1000
                TimePeriod.MONTH -> 30L * 24 * 60 * 60 * 1000
            }
            val sessions = interventionSessionDao.getSessionsInTimeRangeSync(now - duration, now)
            emit(
                sessions.groupBy { session ->
                    Calendar.getInstance().apply { timeInMillis = session.startTime }
                        .get(Calendar.DAY_OF_WEEK)
                }.entries.sortedBy { it.key }.map { (day, items) ->
                    InterventionStat(
                        periodLabel = day.toString(),
                        interventionCount = items.size,
                        timeSavedMinutes = items.sumOf {
                            ((it.endTime - it.startTime) / 60_000L).toInt()
                        }
                    )
                }
            )
        }
    }

    override fun getTopTriggerApps(): Flow<List<AppTrigger>> =
        appUsageDao.getTodayUsage(todayKey()).map { usage ->
            usage.sortedByDescending { it.durationMillis }.take(10).map { item ->
                AppTrigger(
                    id = item.packageName,
                    appName = item.appName,
                    packageName = item.packageName,
                    interventionsTriggered = 0,
                    timeSpentMinutes = (item.durationMillis / 60_000L).toInt(),
                    isMonitored = monitoredPackages.value[item.packageName] ?: false
                )
            }
        }

    override suspend fun toggleAppMonitoring(packageName: String, isMonitored: Boolean) {
        monitoredPackages.value = monitoredPackages.value + (packageName to isMonitored)
    }

    private val monitoredPackages = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    private fun todayKey(): String =
        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            .format(java.util.Date())
}
