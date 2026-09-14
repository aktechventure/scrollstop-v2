package com.aswinkumar.scrollstop.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.usecase.CheckPermissionsUseCase
import com.aswinkumar.scrollstop.presentation.common.ScreenStatus
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
            try {
                val permissions = checkPermissionsUseCase.checkNow()
                _uiState.update {
                    it.copy(
                        status = if (permissions.isFullyGranted) ScreenStatus.Success else ScreenStatus.PermissionRequired,
                        permissionState = permissions,
                        isCompleted = permissions.isFullyGranted
                    )
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(status = ScreenStatus.Error, errorMessage = error.message ?: "Unable to check permissions")
                }
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
