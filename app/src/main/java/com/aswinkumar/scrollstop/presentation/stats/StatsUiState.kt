package com.aswinkumar.scrollstop.presentation.stats

import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod

data class StatsUiState(
    val selectedPeriod: TimePeriod = TimePeriod.WEEK,
    val interventionStats: List<InterventionStat> = emptyList(),
    val topTriggerApps: List<AppTrigger> = emptyList(),
    val totalEstimatedTimeSavedMinutes: Int = 280
)
