package com.aswinkumar.scrollstop.presentation.settings

import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.model.PermissionState

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val permissionState: PermissionState = PermissionState(),
    val showDataDeletionDialog: Boolean = false
)
