package com.aswinkumar.scrollstop.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme
import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.model.PermissionState
import com.aswinkumar.scrollstop.presentation.home.HomeScreen
import com.aswinkumar.scrollstop.presentation.home.HomeUiState
import com.aswinkumar.scrollstop.presentation.home.UsageMetric
import com.aswinkumar.scrollstop.presentation.mindful.MindfulScreen
import com.aswinkumar.scrollstop.presentation.mindful.MindfulUiState
import com.aswinkumar.scrollstop.presentation.onboarding.LandingOnboardingScreen
import com.aswinkumar.scrollstop.presentation.onboarding.OnboardingUiState
import com.aswinkumar.scrollstop.presentation.settings.SettingsScreen
import com.aswinkumar.scrollstop.presentation.settings.SettingsUiState
import com.aswinkumar.scrollstop.presentation.stats.StatsScreen
import com.aswinkumar.scrollstop.presentation.stats.StatsUiState
import org.junit.Rule
import org.junit.Test

class ScrollStopComposeUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun onboarding_showsPermissionRequiredState() {
        composeRule.setContent {
            ScrollStopTheme {
                LandingOnboardingScreen(
                    uiState = OnboardingUiState(
                        currentStep = 0,
                        permissionState = PermissionState(
                            hasUsageAccess = false,
                            hasOverlayPermission = false,
                            hasAccessibilityService = false
                        )
                    ),
                    onRequestUsageAccess = {},
                    onRequestOverlayPermission = {},
                    onRequestAccessibilityService = {},
                    onCompleteOnboarding = {},
                    onNextStep = {},
                    onPrevStep = {}
                )
            }
        }

        composeRule.onNodeWithText("ScrollStop").assertIsDisplayed()
        composeRule.onNodeWithText("Step 1: Usage Access").assertIsDisplayed()
    }

    @Test
    fun home_screen_displaysUsageSummaryWhenDataExists() {
        composeRule.setContent {
            ScrollStopTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        usageMetric = UsageMetric(
                            timeSavedMinutes = 25,
                            interventionsCount = 3,
                            totalScreenTimeMinutes = 90,
                            todayFocusGoal = "Deep Reading & Mindfulness",
                            focusGoalProgress = 0.6f
                        )
                    )
                )
            }
        }

        composeRule.onNodeWithText("Time Saved Today").assertIsDisplayed()
        composeRule.onNodeWithText("Interventions").assertIsDisplayed()
    }

    @Test
    fun settings_screen_displaysDeleteConfirmationState() {
        composeRule.setContent {
            ScrollStopTheme {
                SettingsScreen(
                    uiState = SettingsUiState(
                        settings = AppSettings(),
                        permissionState = PermissionState(
                            hasUsageAccess = true,
                            hasOverlayPermission = true,
                            hasAccessibilityService = true
                        ),
                        showDataDeletionDialog = true
                    ),
                    onRequestUsageAccess = {},
                    onRequestOverlayPermission = {},
                    onRequestAccessibilityService = {},
                    onUpdateDailyLimit = {},
                    onToggleFeed = { _, _ -> },
                    onShowDataDeletionDialog = {},
                    onConfirmClearData = {}
                )
            }
        }

        composeRule.onNodeWithText("Clear All Local Data?").assertIsDisplayed()
    }

    @Test
    fun mindful_screen_showsBreathingAndReflectionContent() {
        composeRule.setContent {
            ScrollStopTheme {
                MindfulScreen(
                    uiState = MindfulUiState(),
                    onStartBreathing = {},
                    onUpdateIntention = {}
                )
            }
        }

        composeRule.onNodeWithText("Mindfulness & Well-being").assertIsDisplayed()
        composeRule.onNodeWithText("Mindful Check-ins").assertIsDisplayed()
    }

    @Test
    fun stats_screen_displaysSummary() {
        composeRule.setContent {
            ScrollStopTheme {
                StatsScreen(
                    uiState = StatsUiState(
                        interventionStats = emptyList(),
                        topTriggerApps = emptyList()
                    ),
                    onPeriodSelected = {},
                    onToggleAppMonitoring = { _, _ -> }
                )
            }
        }

        composeRule.onNodeWithText("Statistics & Insights").assertIsDisplayed()
    }

    @Test
    fun deleteConfirmation_buttonVisible() {
        composeRule.setContent {
            ScrollStopTheme {
                SettingsScreen(
                    uiState = SettingsUiState(
                        settings = AppSettings(),
                        permissionState = PermissionState(),
                        showDataDeletionDialog = true
                    ),
                    onRequestUsageAccess = {},
                    onRequestOverlayPermission = {},
                    onRequestAccessibilityService = {},
                    onUpdateDailyLimit = {},
                    onToggleFeed = { _, _ -> },
                    onShowDataDeletionDialog = {},
                    onConfirmClearData = {}
                )
            }
        }

        composeRule.onNodeWithText("Delete Everything").assertIsDisplayed()
    }

    @Test
    fun settings_screen_supportsDataDeletionAction() {
        composeRule.setContent {
            ScrollStopTheme {
                SettingsScreen(
                    uiState = SettingsUiState(
                        settings = AppSettings(),
                        permissionState = PermissionState(),
                        showDataDeletionDialog = false
                    ),
                    onRequestUsageAccess = {},
                    onRequestOverlayPermission = {},
                    onRequestAccessibilityService = {},
                    onUpdateDailyLimit = {},
                    onToggleFeed = { _, _ -> },
                    onShowDataDeletionDialog = {},
                    onConfirmClearData = {}
                )
            }
        }

        composeRule.onNodeWithText("Clear Local Data & Reset").assertIsDisplayed()
    }

    @Test
    fun onboarding_nextButton_isVisible() {
        composeRule.setContent {
            ScrollStopTheme {
                LandingOnboardingScreen(
                    uiState = OnboardingUiState(currentStep = 0),
                    onRequestUsageAccess = {},
                    onRequestOverlayPermission = {},
                    onRequestAccessibilityService = {},
                    onCompleteOnboarding = {},
                    onNextStep = {},
                    onPrevStep = {}
                )
            }
        }

        composeRule.onNodeWithText("Next").assertIsDisplayed()
    }
}
