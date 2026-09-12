package com.aswinkumar.scrollstop.presentation.mindful

data class MindfulUiState(
    val dailyIntention: String = "I am present and intentional with my screen time today.",
    val breathingSecondsRemaining: Int = 10,
    val isBreathingActive: Boolean = false,
    val totalBreathingSessionsCompleted: Int = 3
)
