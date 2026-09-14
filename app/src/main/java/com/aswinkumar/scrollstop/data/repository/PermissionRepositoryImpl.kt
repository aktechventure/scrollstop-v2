package com.aswinkumar.scrollstop.data.repository

import com.aswinkumar.scrollstop.domain.model.PermissionState
import com.aswinkumar.scrollstop.domain.model.PermissionStatus
import com.aswinkumar.scrollstop.domain.repository.PermissionRepository
import com.aswinkumar.scrollstop.platform.PermissionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionRepositoryImpl(
    private val permissionManager: PermissionManager
) : PermissionRepository {

    private val history = permissionManager.permissionHistory
    private val _permissionState = MutableStateFlow(
        permissionManager.checkAllPermissions().withHistoricalStatuses(history)
    )

    override fun getPermissionState(): Flow<PermissionState> {
        _permissionState.value = checkPermissions()
        return _permissionState.asStateFlow()
    }

    override fun checkPermissions(): PermissionState {
        val current = permissionManager.checkAllPermissions()
        val updated = current.withHistoricalStatuses(history)
        _permissionState.value = updated
        return updated
    }

    private fun PermissionState.withHistoricalStatuses(
        history: PermissionManager.PermissionHistory
    ): PermissionState {
        val usageWasGranted = history.recordIfGranted(
            PermissionManager.PermissionHistory.USAGE_ACCESS,
            hasUsageAccess
        )
        val overlayWasGranted = history.recordIfGranted(
            PermissionManager.PermissionHistory.OVERLAY,
            hasOverlayPermission
        )
        val accessibilityWasGranted = history.recordIfGranted(
            PermissionManager.PermissionHistory.ACCESSIBILITY,
            hasAccessibilityService
        )
        return copy(
            usageAccessStatus = status(hasUsageAccess, usageWasGranted),
            overlayPermissionStatus = status(hasOverlayPermission, overlayWasGranted),
            accessibilityServiceStatus = status(hasAccessibilityService, accessibilityWasGranted)
        )
    }

    private fun status(granted: Boolean, wasPreviouslyGranted: Boolean): PermissionStatus =
        PermissionStatus.resolve(granted, wasPreviouslyGranted)
}
