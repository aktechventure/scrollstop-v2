package com.aswinkumar.scrollstop.platform

import android.app.usage.UsageStatsManager
import android.content.Context
import android.app.AppOpsManager
import android.os.Process
import com.aswinkumar.scrollstop.data.local.dao.AppUsageDao
import com.aswinkumar.scrollstop.data.local.entity.AppUsageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UsageStatsDataSource(
    context: Context,
    private val appUsageDao: AppUsageDao
) {
    private val appContext = context.applicationContext
    private val usageStatsManager =
        appContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    suspend fun refreshToday(): String = withContext(Dispatchers.IO) {
        if (!hasUsageAccess()) throw SecurityException("Usage access permission required")
        val start = todayStart()
        val end = System.currentTimeMillis()
        val today = dateKey(start)
        val packageManager = appContext.packageManager
        val usage = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
            .filter { it.totalTimeInForeground > 0L }
            .map {
                AppUsageEntity(
                    date = today,
                    timestamp = end,
                    packageName = it.packageName,
                    appName = runCatching {
                        packageManager.getApplicationLabel(
                            packageManager.getApplicationInfo(it.packageName, 0)
                        ).toString()
                    }.getOrDefault(it.packageName),
                    durationMillis = it.totalTimeInForeground
                )
            }

            private fun hasUsageAccess(): Boolean {
                val appOps = appContext.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
                    ?: return false
                return appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    appContext.packageName
                ) == AppOpsManager.MODE_ALLOWED
            }
        if (usage.isNotEmpty()) appUsageDao.insertUsages(usage)
        today
    }

    private fun todayStart(): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun dateKey(timestamp: Long): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(timestamp))
}
