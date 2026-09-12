package com.aswinkumar.scrollstop.presentation.onboarding

import com.aswinkumar.scrollstop.domain.model.PermissionState

data class OnboardingUiState(
    val currentStep: Int = 0, // 0: Welcome/Usage, 1: Overlay, 2: Accessibility
    val permissionState: PermissionState = PermissionState(),
    val isCompleted: Boolean = false
)
