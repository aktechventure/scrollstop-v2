package com.aswinkumar.scrollstop.domain.repository

import com.aswinkumar.scrollstop.domain.model.PermissionState
import kotlinx.coroutines.flow.Flow

interface PermissionRepository {
    fun getPermissionState(): Flow<PermissionState>
    fun checkPermissions(): PermissionState
}
