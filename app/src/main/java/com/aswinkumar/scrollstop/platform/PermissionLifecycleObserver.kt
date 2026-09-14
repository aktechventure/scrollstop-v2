package com.aswinkumar.scrollstop.platform

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aswinkumar.scrollstop.presentation.onboarding.OnboardingViewModel
import com.aswinkumar.scrollstop.presentation.settings.SettingsViewModel

class PermissionLifecycleObserver(
    private val onboardingViewModel: OnboardingViewModel,
    private val settingsViewModel: SettingsViewModel
) : DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        onboardingViewModel.refreshPermissions()
        settingsViewModel.refreshPermissions()
    }
}
