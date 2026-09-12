package com.aswinkumar.scrollstop.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.usecase.CheckPermissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val checkPermissionsUseCase: CheckPermissionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        refreshPermissions()
    }

    fun refreshPermissions() {
        viewModelScope.launch {
            val permissions = checkPermissionsUseCase.checkNow()
            _uiState.update {
                it.copy(
                    permissionState = permissions,
                    isCompleted = permissions.isFullyGranted
                )
            }
        }
    }

    fun nextStep() {
        _uiState.update {
            val next = (it.currentStep + 1).coerceAtMost(2)
            it.copy(currentStep = next)
        }
    }

    fun prevStep() {
        _uiState.update {
            val prev = (it.currentStep - 1).coerceAtLeast(0)
            it.copy(currentStep = prev)
        }
    }
}
