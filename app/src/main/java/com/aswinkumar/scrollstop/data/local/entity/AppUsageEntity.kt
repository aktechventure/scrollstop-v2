package com.aswinkumar.scrollstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_usage")
data class AppUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val timestamp: Long,
    val packageName: String,
    val appName: String,
    val durationMillis: Long,
    val sessionCount: Int = 1,
    val sessionInfo: String? = null
)
