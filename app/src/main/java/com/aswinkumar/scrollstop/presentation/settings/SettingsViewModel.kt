package com.aswinkumar.scrollstop.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswinkumar.scrollstop.domain.usecase.CheckPermissionsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val checkPermissionsUseCase: CheckPermissionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        refreshPermissions()
    }

    fun loadSettings() {
        viewModelScope.launch {
            getSettingsUseCase().collect { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
        }
    }

    fun refreshPermissions() {
        viewModelScope.launch {
            val state = checkPermissionsUseCase.checkNow()
            _uiState.update { it.copy(permissionState = state) }
        }
    }

    fun updateDailyLimit(minutes: Int) {
        viewModelScope.launch {
            getSettingsUseCase.updateLimit(minutes)
        }
    }

    fun toggleFeed(packageName: String, enabled: Boolean) {
        viewModelScope.launch {
            getSettingsUseCase.toggleFeed(packageName, enabled)
        }
    }

    fun showDataDeletionDialog(show: Boolean) {
        _uiState.update { it.copy(showDataDeletionDialog = show) }
    }

    fun clearAllUserData() {
        viewModelScope.launch {
            getSettingsUseCase.clearData()
            _uiState.update { it.copy(showDataDeletionDialog = false) }
        }
    }
}
