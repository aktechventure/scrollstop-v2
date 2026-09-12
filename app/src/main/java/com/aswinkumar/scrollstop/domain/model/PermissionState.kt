package com.aswinkumar.scrollstop.domain.model

data class PermissionState(
    val hasUsageAccess: Boolean = false,
    val hasOverlayPermission: Boolean = false,
    val hasAccessibilityService: Boolean = false
) {
    val isFullyGranted: Boolean
        get() = hasUsageAccess && hasOverlayPermission && hasAccessibilityService
}

data class AppSettings(
    val dailyTimeLimitMinutes: Int = 45,
    val notificationsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val feedsList: List<FeedItemSetting> = listOf(
        FeedItemSetting("Instagram Reels", "com.instagram.android", true),
        FeedItemSetting("YouTube Shorts", "com.google.android.youtube", true),
        FeedItemSetting("TikTok Feed", "com.zhiliaoapp.musically", true),
        FeedItemSetting("X / Twitter Timeline", "com.twitter.android", false)
    )
)

data class FeedItemSetting(
    val name: String,
    val packageName: String,
    val isEnabled: Boolean
)
