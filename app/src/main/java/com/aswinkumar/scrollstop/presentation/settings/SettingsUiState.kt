package com.aswinkumar.scrollstop.presentation.settings

import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.model.PermissionState
import com.aswinkumar.scrollstop.presentation.common.ScreenStatus

data class SettingsUiState(
    val status: ScreenStatus = ScreenStatus.Loading,
    val errorMessage: String? = null,
    val settings: AppSettings = AppSettings(),
    val permissionState: PermissionState = PermissionState(),
    val showDataDeletionDialog: Boolean = false
)
