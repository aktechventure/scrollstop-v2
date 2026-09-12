package com.aswinkumar.scrollstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme
import com.aswinkumar.scrollstop.data.repository.PermissionRepositoryImpl
import com.aswinkumar.scrollstop.data.repository.SettingsRepositoryImpl
import com.aswinkumar.scrollstop.data.repository.UsageRepositoryImpl
import com.aswinkumar.scrollstop.domain.usecase.CheckPermissionsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetSettingsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetStatisticsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetTodayMetricsUseCase
import com.aswinkumar.scrollstop.platform.PermissionManager
import com.aswinkumar.scrollstop.presentation.home.HomeViewModel
import com.aswinkumar.scrollstop.presentation.main.MainScreen
import com.aswinkumar.scrollstop.presentation.mindful.MindfulViewModel
import com.aswinkumar.scrollstop.presentation.onboarding.OnboardingViewModel
import com.aswinkumar.scrollstop.presentation.settings.SettingsViewModel
import com.aswinkumar.scrollstop.presentation.stats.StatsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Platform & Data Layers
        val permissionManager = PermissionManager(applicationContext)
        val usageRepository = UsageRepositoryImpl()
        val settingsRepository = SettingsRepositoryImpl()
        val permissionRepository = PermissionRepositoryImpl(permissionManager)

        // Setup Domain UseCases
        val checkPermissionsUseCase = CheckPermissionsUseCase(permissionRepository)
        val getTodayMetricsUseCase = GetTodayMetricsUseCase(usageRepository)
        val getStatisticsUseCase = GetStatisticsUseCase(usageRepository)
        val getSettingsUseCase = GetSettingsUseCase(settingsRepository)

        // Setup ViewModels
        val onboardingViewModel = OnboardingViewModel(checkPermissionsUseCase)
        val homeViewModel = HomeViewModel(getTodayMetricsUseCase)
        val statsViewModel = StatsViewModel(getStatisticsUseCase, usageRepository)
        val mindfulViewModel = MindfulViewModel()
        val settingsViewModel = SettingsViewModel(getSettingsUseCase, checkPermissionsUseCase)

        setContent {
            ScrollStopTheme {
                MainScreen(
                    onboardingViewModel = onboardingViewModel,
                    homeViewModel = homeViewModel,
                    statsViewModel = statsViewModel,
                    mindfulViewModel = mindfulViewModel,
                    settingsViewModel = settingsViewModel,
                    permissionManager = permissionManager
                )
            }
        }
    }
}
