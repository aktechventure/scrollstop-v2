package com.aswinkumar.scrollstop.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aswinkumar.scrollstop.platform.PermissionManager
import com.aswinkumar.scrollstop.presentation.home.HomeScreen
import com.aswinkumar.scrollstop.presentation.home.HomeViewModel
import com.aswinkumar.scrollstop.presentation.mindful.MindfulScreen
import com.aswinkumar.scrollstop.presentation.mindful.MindfulViewModel
import com.aswinkumar.scrollstop.presentation.navigation.Screen
import com.aswinkumar.scrollstop.presentation.navigation.ScrollStopNavBar
import com.aswinkumar.scrollstop.presentation.onboarding.LandingOnboardingScreen
import com.aswinkumar.scrollstop.presentation.onboarding.OnboardingViewModel
import com.aswinkumar.scrollstop.presentation.settings.SettingsScreen
import com.aswinkumar.scrollstop.presentation.settings.SettingsViewModel
import com.aswinkumar.scrollstop.presentation.stats.StatsScreen
import com.aswinkumar.scrollstop.presentation.stats.StatsViewModel

@Composable
fun MainScreen(
    onboardingViewModel: OnboardingViewModel,
    homeViewModel: HomeViewModel,
    statsViewModel: StatsViewModel,
    mindfulViewModel: MindfulViewModel,
    settingsViewModel: SettingsViewModel,
    permissionManager: PermissionManager,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val onboardingUiState by onboardingViewModel.uiState.collectAsState()
    val homeUiState by homeViewModel.uiState.collectAsState()
    val statsUiState by statsViewModel.uiState.collectAsState()
    val mindfulUiState by mindfulViewModel.uiState.collectAsState()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val isOnboardingRoute = currentRoute == Screen.Onboarding.route
    val startDestination = remember {
        if (onboardingUiState.permissionState.isFullyGranted) Screen.Home.route else Screen.Onboarding.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isOnboardingRoute) {
                ScrollStopNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Onboarding.route) {
                LandingOnboardingScreen(
                    uiState = onboardingUiState,
                    onRequestUsageAccess = {
                        context.startActivity(permissionManager.createUsageAccessIntent())
                    },
                    onRequestOverlayPermission = {
                        context.startActivity(permissionManager.createOverlayPermissionIntent())
                    },
                    onRequestAccessibilityService = {
                        context.startActivity(permissionManager.createAccessibilitySettingsIntent())
                    },
                    onCompleteOnboarding = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    },
                    onNextStep = { onboardingViewModel.nextStep() },
                    onPrevStep = { onboardingViewModel.prevStep() }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    uiState = homeUiState
                )
            }

            composable(Screen.Stats.route) {
                StatsScreen(
                    uiState = statsUiState,
                    onPeriodSelected = { statsViewModel.selectPeriod(it) },
                    onToggleAppMonitoring = { pkg, monitored ->
                        statsViewModel.toggleAppMonitoring(pkg, monitored)
                    }
                )
            }

            composable(Screen.Mindful.route) {
                MindfulScreen(
                    uiState = mindfulUiState,
                    onStartBreathing = { mindfulViewModel.startBreathingExercise() },
                    onUpdateIntention = { mindfulViewModel.updateDailyIntention(it) }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    uiState = settingsUiState,
                    onRequestUsageAccess = {
                        context.startActivity(permissionManager.createUsageAccessIntent())
                    },
                    onRequestOverlayPermission = {
                        context.startActivity(permissionManager.createOverlayPermissionIntent())
                    },
                    onRequestAccessibilityService = {
                        context.startActivity(permissionManager.createAccessibilitySettingsIntent())
                    },
                    onUpdateDailyLimit = { settingsViewModel.updateDailyLimit(it) },
                    onToggleFeed = { pkg, enabled -> settingsViewModel.toggleFeed(pkg, enabled) },
                    onShowDataDeletionDialog = { settingsViewModel.showDataDeletionDialog(it) },
                    onConfirmClearData = { settingsViewModel.clearAllUserData() }
                )
            }
        }
    }
}
