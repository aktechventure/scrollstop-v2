package com.aswinkumar.scrollstop.domain.model

data class UsageMetric(
    val timeSavedMinutes: Int,
    val interventionsCount: Int,
    val totalScreenTimeMinutes: Int,
    val todayFocusGoal: String,
    val focusGoalProgress: Float
)

data class InterventionStat(
    val periodLabel: String,
    val interventionCount: Int,
    val timeSavedMinutes: Int
)

enum class TimePeriod {
    DAY, WEEK, MONTH
}

data class AppTrigger(
    val id: String,
    val appName: String,
    val packageName: String,
    val interventionsTriggered: Int,
    val timeSpentMinutes: Int,
    val isMonitored: Boolean
)
