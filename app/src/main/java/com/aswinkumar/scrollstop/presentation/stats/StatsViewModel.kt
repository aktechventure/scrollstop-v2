package com.aswinkumar.scrollstop.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.repository.UsageRepository
import com.aswinkumar.scrollstop.domain.usecase.GetStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(
    private val getStatisticsUseCase: GetStatisticsUseCase,
    private val usageRepository: UsageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        selectPeriod(TimePeriod.WEEK)
        loadTopTriggers()
    }

    fun selectPeriod(period: TimePeriod) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedPeriod = period) }
            getStatisticsUseCase.getStats(period).collect { stats ->
                val totalSaved = stats.sumOf { it.timeSavedMinutes }
                _uiState.update {
                    it.copy(
                        interventionStats = stats,
                        totalEstimatedTimeSavedMinutes = totalSaved
                    )
                }
            }
        }
    }

    private fun loadTopTriggers() {
        viewModelScope.launch {
            getStatisticsUseCase.getTopTriggers().collect { apps ->
                _uiState.update { it.copy(topTriggerApps = apps) }
            }
        }
    }

    fun toggleAppMonitoring(packageName: String, isMonitored: Boolean) {
        viewModelScope.launch {
            usageRepository.toggleAppMonitoring(packageName, isMonitored)
        }
    }
}
