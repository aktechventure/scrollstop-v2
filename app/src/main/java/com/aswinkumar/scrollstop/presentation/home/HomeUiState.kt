package com.aswinkumar.scrollstop.presentation.home

import com.aswinkumar.scrollstop.domain.model.UsageMetric
import com.aswinkumar.scrollstop.presentation.common.ScreenStatus

data class HomeUiState(
    val isLoading: Boolean = false,
    val status: ScreenStatus = ScreenStatus.Loading,
    val errorMessage: String? = null,
    val greeting: String = "Good morning, You're doing great",
    val usageMetric: UsageMetric? = null
)
