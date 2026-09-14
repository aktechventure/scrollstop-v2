package com.aswinkumar.scrollstop.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.model.TimePeriod
import com.aswinkumar.scrollstop.domain.usecase.GetStatisticsUseCase
import com.aswinkumar.scrollstop.domain.usecase.ToggleAppMonitoringUseCase
import com.aswinkumar.scrollstop.presentation.common.ScreenStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

class StatsViewModel(
    private val getStatisticsUseCase: GetStatisticsUseCase,
    private val toggleAppMonitoringUseCase: ToggleAppMonitoringUseCase
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
            try {
                getStatisticsUseCase.getStats(period).collect { stats ->
                    val totalSaved = stats.sumOf { it.timeSavedMinutes }
                    _uiState.update {
                        it.copy(
                            status = if (stats.isEmpty()) ScreenStatus.Empty else ScreenStatus.Success,
                            errorMessage = null,
                            interventionStats = stats,
                            totalEstimatedTimeSavedMinutes = totalSaved
                        )
                    }
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _uiState.update {
                    it.copy(status = ScreenStatus.Error, errorMessage = error.message ?: "Unable to load statistics")
                }
            }
        }
    }

    private fun loadTopTriggers() {
        viewModelScope.launch {
            try {
                getStatisticsUseCase.getTopTriggers().collect { apps ->
                    _uiState.update { it.copy(topTriggerApps = apps) }
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _uiState.update {
                    it.copy(status = ScreenStatus.Error, errorMessage = error.message ?: "Unable to load trigger apps")
                }
            }
        }
    }

    fun toggleAppMonitoring(packageName: String, isMonitored: Boolean) {
        viewModelScope.launch {
            toggleAppMonitoringUseCase(packageName, isMonitored)
        }
    }
}
