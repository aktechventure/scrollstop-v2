package com.aswinkumar.scrollstop.presentation.home

import com.aswinkumar.scrollstop.domain.model.UsageMetric

data class HomeUiState(
    val isLoading: Boolean = false,
    val greeting: String = "Good morning, You're doing great",
    val usageMetric: UsageMetric = UsageMetric(
        timeSavedMinutes = 42,
        interventionsCount = 14,
        totalScreenTimeMinutes = 108,
        todayFocusGoal = "Deep Reading & Mindfulness",
        focusGoalProgress = 0.72f
    )
)
