package com.aswinkumar.scrollstop.domain.engine

import com.aswinkumar.scrollstop.data.local.entity.AppUsageEntity

class InsightEngine {
    fun summarize(usage: List<AppUsageEntity>): String? {
        val longest = usage.maxByOrNull { it.durationMillis } ?: return null
        return "${longest.appName} was your longest recorded session today."
    }
}
