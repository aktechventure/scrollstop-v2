package com.aswinkumar.scrollstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey val id: Int = 1,
    val notificationsEnabled: Boolean = true,
    val quietHoursStart: String = "22:00",
    val quietHoursEnd: String = "07:00",
    val dailyQuestionLimit: Int = 5,
    val dailyTimeLimitMinutes: Int = 45,
    val targetedApps: String = "com.google.android.youtube,com.instagram.android,com.zhiliaoapp.musically",
    val themePreference: String = "SYSTEM"
)
