package com.aswinkumar.scrollstop

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme
import com.aswinkumar.scrollstop.data.repository.PermissionRepositoryImpl
import com.aswinkumar.scrollstop.data.repository.SettingsRepositoryImpl
import com.aswinkumar.scrollstop.data.repository.UsageRepositoryImpl
import com.aswinkumar.scrollstop.data.local.ScrollStopDatabaseProvider
import com.aswinkumar.scrollstop.domain.usecase.CheckPermissionsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetSettingsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetStatisticsUseCase
import com.aswinkumar.scrollstop.domain.usecase.GetTodayMetricsUseCase
import com.aswinkumar.scrollstop.domain.usecase.ToggleAppMonitoringUseCase
import com.aswinkumar.scrollstop.platform.UsageStatsDataSource
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
        val database = ScrollStopDatabaseProvider.get(applicationContext)
        val usageRepository = UsageRepositoryImpl(
            appUsageDao = database.appUsageDao(),
            interventionSessionDao = database.interventionSessionDao(),
            usageStatsDataSource = UsageStatsDataSource(applicationContext, database.appUsageDao())
        )
        val settingsRepository = SettingsRepositoryImpl(database)
        val permissionRepository = PermissionRepositoryImpl(permissionManager)

        // Setup Domain UseCases
        val checkPermissionsUseCase = CheckPermissionsUseCase(permissionRepository)
        val getTodayMetricsUseCase = GetTodayMetricsUseCase(usageRepository)
        val getStatisticsUseCase = GetStatisticsUseCase(usageRepository)
        val toggleAppMonitoringUseCase = ToggleAppMonitoringUseCase(usageRepository)
        val getSettingsUseCase = GetSettingsUseCase(settingsRepository)

        // Setup ViewModels
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
                modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
                    OnboardingViewModel(checkPermissionsUseCase) as T
                modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(getTodayMetricsUseCase) as T
                modelClass.isAssignableFrom(StatsViewModel::class.java) ->
                    StatsViewModel(getStatisticsUseCase, toggleAppMonitoringUseCase) as T
                modelClass.isAssignableFrom(MindfulViewModel::class.java) -> MindfulViewModel() as T
                modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                    SettingsViewModel(getSettingsUseCase, checkPermissionsUseCase) as T
                else -> throw IllegalArgumentException("Unknown ViewModel ${modelClass.name}")
            }
        }
        val onboardingViewModel = ViewModelProvider(this, factory)[OnboardingViewModel::class.java]
        val homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        val statsViewModel = ViewModelProvider(this, factory)[StatsViewModel::class.java]
        val mindfulViewModel = ViewModelProvider(this, factory)[MindfulViewModel::class.java]
        val settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

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
