package com.aswinkumar.scrollstop.domain.model

enum class PermissionStatus {
    REQUIRED,
    DENIED,
    GRANTED;

    companion object {
        fun resolve(granted: Boolean, wasPreviouslyGranted: Boolean): PermissionStatus =
            when {
                granted -> GRANTED
                wasPreviouslyGranted -> DENIED
                else -> REQUIRED
            }
    }
}

data class PermissionState(
    val hasUsageAccess: Boolean = false,
    val hasOverlayPermission: Boolean = false,
    val hasAccessibilityService: Boolean = false,
    val usageAccessStatus: PermissionStatus =
        if (hasUsageAccess) PermissionStatus.GRANTED else PermissionStatus.REQUIRED,
    val overlayPermissionStatus: PermissionStatus =
        if (hasOverlayPermission) PermissionStatus.GRANTED else PermissionStatus.REQUIRED,
    val accessibilityServiceStatus: PermissionStatus =
        if (hasAccessibilityService) PermissionStatus.GRANTED else PermissionStatus.REQUIRED
) {
    val isFullyGranted: Boolean
        get() = hasUsageAccess && hasOverlayPermission && hasAccessibilityService
}

data class AppSettings(
    val dailyTimeLimitMinutes: Int = 45,
    val notificationsEnabled: Boolean = true,
    val quietHoursStart: String = "22:00",
    val quietHoursEnd: String = "07:00",
    val interventionThresholdMinutes: Int = 30,
    val privacyModeEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val feedsList: List<FeedItemSetting> = listOf(
        FeedItemSetting("YouTube", "com.google.android.youtube", true),
        FeedItemSetting("Instagram", "com.instagram.android", true)
    )
)

data class FeedItemSetting(
    val name: String,
    val packageName: String,
    val isEnabled: Boolean
)
