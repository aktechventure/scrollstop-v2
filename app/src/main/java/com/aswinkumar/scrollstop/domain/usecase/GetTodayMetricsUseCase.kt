package com.aswinkumar.scrollstop.domain.usecase

import com.aswinkumar.scrollstop.domain.model.UsageMetric
import com.aswinkumar.scrollstop.domain.repository.UsageRepository
import kotlinx.coroutines.flow.Flow

class GetTodayMetricsUseCase(
    private val repository: UsageRepository
) {
    operator fun invoke(): Flow<UsageMetric> = repository.getTodayMetrics()
}
