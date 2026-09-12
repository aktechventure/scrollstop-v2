package com.aswinkumar.scrollstop.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.usecase.GetTodayMetricsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getTodayMetricsUseCase: GetTodayMetricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            getTodayMetricsUseCase().collect { metric ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        usageMetric = metric
                    )
                }
            }
        }
    }
}
