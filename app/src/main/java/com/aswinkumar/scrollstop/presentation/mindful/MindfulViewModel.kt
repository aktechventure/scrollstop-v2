package com.aswinkumar.scrollstop.presentation.mindful

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MindfulViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MindfulUiState())
    val uiState: StateFlow<MindfulUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startBreathingExercise() {
        if (_uiState.value.isBreathingActive) return

        _uiState.update {
            it.copy(
                isBreathingActive = true,
                breathingSecondsRemaining = 10
            )
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.breathingSecondsRemaining > 0) {
                delay(1000)
                _uiState.update {
                    it.copy(breathingSecondsRemaining = it.breathingSecondsRemaining - 1)
                }
            }
            _uiState.update {
                it.copy(
                    isBreathingActive = false,
                    totalBreathingSessionsCompleted = it.totalBreathingSessionsCompleted + 1
                )
            }
        }
    }

    fun updateDailyIntention(newIntention: String) {
        _uiState.update { it.copy(dailyIntention = newIntention) }
    }
}
