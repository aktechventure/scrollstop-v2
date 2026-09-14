package com.aswinkumar.scrollstop.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.usecase.GetTodayMetricsUseCase
import com.aswinkumar.scrollstop.presentation.common.ScreenStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

class HomeViewModel(
    private val getTodayMetricsUseCase: GetTodayMetricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true, status = ScreenStatus.Loading))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                getTodayMetricsUseCase().collect { metric ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            status = if (metric.totalScreenTimeMinutes == 0) {
                                ScreenStatus.Empty
                            } else {
                                ScreenStatus.Success
                            },
                            errorMessage = null,
                            usageMetric = metric
                        )
                    }
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        status = if (error is SecurityException) {
                            ScreenStatus.PermissionRequired
                        } else {
                            ScreenStatus.Error
                        },
                        errorMessage = error.message ?: "Unable to load today's usage"
                    )
                }
            }
        }
    }
}
