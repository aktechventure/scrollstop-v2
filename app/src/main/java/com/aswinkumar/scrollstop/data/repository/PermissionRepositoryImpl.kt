package com.aswinkumar.scrollstop.data.repository

import com.aswinkumar.scrollstop.domain.model.PermissionState
import com.aswinkumar.scrollstop.domain.repository.PermissionRepository
import com.aswinkumar.scrollstop.platform.PermissionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionRepositoryImpl(
    private val permissionManager: PermissionManager
) : PermissionRepository {

    private val _permissionState = MutableStateFlow(permissionManager.checkAllPermissions())

    override fun getPermissionState(): Flow<PermissionState> {
        _permissionState.value = permissionManager.checkAllPermissions()
        return _permissionState.asStateFlow()
    }

    override fun checkPermissions(): PermissionState {
        val updated = permissionManager.checkAllPermissions()
        _permissionState.value = updated
        return updated
    }
}
