package com.aswinkumar.scrollstop.domain.usecase

import com.aswinkumar.scrollstop.domain.model.PermissionState
import com.aswinkumar.scrollstop.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.Flow

class CheckPermissionsUseCase(
    private val repository: PermissionRepository
) {
    fun observeState(): Flow<PermissionState> = repository.getPermissionState()
    fun checkNow(): PermissionState = repository.checkPermissions()
}
